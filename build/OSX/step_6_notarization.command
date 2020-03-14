cd "`dirname $0`"
xcrun altool --notarize-app -f ./build/SPARCK*.dmg -t osx -u 'concept@maybites.ch' -p "@keychain:AD_PASSWORD" -primary-bundle-id com.tecartlab.sparck --asc-provider 'GR53GTQ9C7'