#!/usr/bin/env node

// execute this script from finder with the step_07_codeSign.command

// REQUIREMENTS:
// nodejs needs to be installed:
// easiest way to do this: install homebrew from https://brew.sh
// once installed, use 'brew install node' to install all dependencies.

const fs = require('fs');
const path = require('path');
const child_process = require('child_process');

var IDENTITY = ""; // needs to be set as command line argument

// print process.argv
process.argv.forEach(function (val, index, array) {
  if(index == 2){
    IDENTITY = val;
  }
});

if(IDENTITY == ""){
  console.log("ERROR:");
  console.log("You need to provide the TeamID (also known as --asc-provider) as a command line argument");
  process.exit(0);  
}

/**
 * Explores recursively a directory and returns all the filepaths and folderpaths in the callback.
 *
 * @see http://stackoverflow.com/a/5827895/4241030
 * @param {String} dir
 * @param {Function} done
 */
function filewalker(dir, done) {
    let results = [];

    fs.readdir(dir, function(err, list) {
        if (err) return done(err);

        var pending = list.length;

        if (!pending) return done(null, results);

        list.forEach(function(file){
            file = path.resolve(dir, file);

            fs.stat(file, function(err, stat){
                // If directory, execute a recursive call
                if (stat && stat.isDirectory()) {
                    // Add directory to array [comment if you need to remove the directories from the array]
                    results.push(file);

                    filewalker(file, function(err, res){
                        results = results.concat(res);
                        if (!--pending) done(null, results);
                    });
                } else {
                    results.push(file);

                    if (!--pending) done(null, results);
                }
            });
        });
    });
};

/**
 * Executes terminal commands.
 *
 * @param {String} cmd
 */
function exec(cmd) {
    console.log(cmd);
    const result = child_process.spawnSync(cmd, {shell: true, stdio: 'inherit'});
    if (result.status !== 0) {
        console.log(`Command failed with status ${result.status}`);
        if (result.error) console.log(result.error);
        process.exit(1);
    }
}

const APP = __dirname + "/build/SPARCK.app";
const ENTITLEMENTS = "../Bundle/SPARCK.entitlement";

const items = [];

const mxjDir = `${APP}/Contents/Resources/C74/packages/max-mxj`;
const syphonDir = `${APP}/Contents/Resources/C74/packages/Syphon`;

// workhorse. will first grab all files under mxjDir
filewalker(mxjDir, function(err, data){
  if(err){
      throw err;
  }
  // go throuh all files and
  for(var i = 0; i < data.length; i++){
    // add all dylibs to the items list
    var re = /.dylib/;
    if(data[i].match(re) != null){
      items.push(data[i]);
    }
  }

  // add custom paths:
  items.push(mxjDir + "/jre/Contents/Home/bin/java");
  items.push(mxjDir + "/externals/mxj.mxo");
  items.push(mxjDir + "/externals/mxj~.mxo");
  items.push(mxjDir + "/extensions/mxj_safe.mxo");

  items.push(syphonDir + "/externals/jit.gl.syphonclient.mxo");
  items.push(syphonDir + "/externals/jit.gl.syphonserver.mxo");

  console.log("");
  console.log("### codesigning... ");
  for (const item of items) {
      exec(`codesign --verbose --force --deep --options runtime --timestamp --sign "${IDENTITY}" --entitlements "${ENTITLEMENTS}" "${item}"`);
  }

  exec(`codesign --verbose --force --deep --options runtime --timestamp --sign "${IDENTITY}" --entitlements "${ENTITLEMENTS}" "${APP}"`);

  /****************************************************************************/

  console.log("");
  console.log("### verifying signature");

  exec(`codesign --verify -vvvv "${APP}"`);
});
