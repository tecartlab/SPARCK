cd "`dirname $0`"
xattr -cr ./build/SPARCK.app
codesign -s GR53GTQ9C7 --options runtime --timestamp --deep --entitlements ../Bundle/SPARCK.entitlement -f ./build/SPARCK.app
