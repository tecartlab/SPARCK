# SPARCK

SPatial Augmented Reality Contruction Kit.

Also includes QueScript.

## Installation

There are three flavors of SPARCK:

* The standalone app.
* Running the app inside Max8. You need [Max8](https://cycling74.com/downloads/) and the SPARCK package, but there is no need for a Max8 license.
* The SPARCK package for creating [Max](https://cycling74.com/downloads/) patches based on SPARCK nodes. This allows to use the full power of Max8 in combination with SPARCK. This requires a [Max8 license](https://cycling74.com/shop).

### Download

#### Standalone APP
get the latest [Release](https://github.com/tecartlab/SPARCK/Releases) for your operating system.

#### Max Package
get the latest [SPARCK package](https://github.com/tecartlab/SPARCK/Releases) for [Max](https://cycling74.com/downloads/). This package also includes the QueScript abstraction.

find out more about [Max packages](https://docs.cycling74.com/max8/vignettes/package_manager)

IMPORTANT: Some of Sparck's java externals/dependencies are compiled with Java13. A consequence is you need to hava Java13 installed on your machine, which also means you need [MXJ](https://github.com/tecartlab/max-mxj) max package installed, which overrides Max8 internal mxj package that can only deal with Java 8.

## Usage

see [tecartlab.com](http://tecartlab.com) for more information.

## Building

see the [wiki](https://github.com/tecartlab/SPARCK/wiki) for instructions on how to build SPARCK.

## Credits

by maybites (2012-2020)

based on

* [Max8 Framework](https://cycling74.com/downloads/)
* portions of [obj loader](https://code.google.com/archive/p/saitoobjloader/)
* portions of [three.js](https://threejs.org/)
* portions of [EvalEx](https://github.com/uklimaschewski/EvalEx)
* some snippets of code from [OpenFrameWorks](http://openframeworks.cc)

includes / requires

* [Java 13](https://jdk.java.net) JDK 13
* [MXJ](https://github.com/tecartlab/max-mxj) max package
* [OpenCV](https://opencv.org/) for Java
* [Syphon](http://syphon.v002.info/) max package (OSX only)
* [Spout](spout.zeal.co) max package (WIN only)
* [HAP](cycling74.com/toolbox/jit-gl-hap/) max package
* [Worldmaking](https://github.com/worldmaking/Max_Worldmaking_Package) max package (WIN only)
* [shell](https://github.com/jeremybernstein/shell) max external
* [toxiclib](http://toxiclibs.org/) for Processing
* [PixelPusher-java](https://github.com/robot-head/PixelPusher) Java library
