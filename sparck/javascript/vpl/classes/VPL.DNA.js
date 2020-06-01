/* MIT License
 *
 * Copyright (c) 2012-2020 tecartlab.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author maybites
 *
 */
 
VPL.DNA = function () {

    this.localVar = 0;
    this.baseDB = null;
    
    this.importDB = null;
    this.exportDB = null;
    
    this.init();
};

VPL.DNA.prototype = {

	constructor: VPL.DNA,

    init: function ( ) {
        baseDB = new Dict("vpl::db");
    },

    load: function ( _path ) {
        post("loading "+_path+"... \n");
        this.importDB = new Dict();
        this.importDB.readany(_path);
        
        if (this.importDB.contains("patcher")){
            //it is the new fileformat
            post("... its the new file format \n");
        } else {
            post("... its another file format \n");
            var Exporter = new VPL.DNA.export_V0toMax();
            Exporter.debugger(true);
            Exporter.load(_path);
        }        
    }    
    
};
 