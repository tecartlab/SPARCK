# HOWTO Build OSX application

Follow the scripts numbers:

0. Apply the new version and build number inside eclipse.
1. first open MaxMSP and export the Sparck Patch as an application
  - with the provided 'build_max_script'
  - save inside the 'OSX/build' folder where this readme resides and
  - call it 'SPARCK.app'
2. edit the Info file and apply the new version and build numbers.
3. execute the apple script buildFinalize
4. codesign the new app
5. open DropDMG
  - make sure under Preferences > Configurations > signing: the current codesigning certificate is selected
  - drag and drop the app into the app window
  - simplify the dmg file name
6. notarize the dmg this will take some time (uploading to and notarizing by the apple server)
7. check the progress. an email arrives once the process has been completed
8. staple the dmg
9. zip the dmg.
10. upload the zip to the server.

# HOWTO Build Windows application

Follow the scripts numbers:

0. Apply the new version and build number inside eclipse.
1. first open MaxMSP and export the Sparck Patch as an application
  - with the provided 'step_1_build_max_script'
  - save inside the 'WIN/build' folder where this readme resides and
  - call it 'SPARCK.app'
2. execute the script buildFinalize
  - this requires a JRE at C:\Program Files\Java\jre, which is copied to the correct location
3. edit the executables properties
  - start RecourceHacker http://www.angusj.com/resourcehacker/
  - load SPARCK.exe
  - go to Version Info -> 1 : 1033
  - edit the app infos
  - Compile script (green arrow)
  - save
4. create the installer
  - run the inno script
