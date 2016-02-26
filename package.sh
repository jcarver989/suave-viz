#!/usr/bin/env bash
#VERSION=`cat build.sbt | grep version | sed -n 's/version := "\(.*\)",/\1/p'`
sbt clean assembly
VERSION="1.1.0"
DIR="suave-viz-$VERSION"
mkdir -p $DIR/bin
cp target/scala-2.11/suaveviz-assembly-$VERSION.jar $DIR

SCRIPT=$DIR/bin/suave
echo "#!/usr/bin/env bash" >> $SCRIPT
echo "DIR=\"\$( cd \"\$( dirname \"\${BASH_SOURCE[0]}\" )\" && pwd )\"" >> $SCRIPT
echo "java -jar \$DIR/../suaveviz-assembly-$VERSION.jar \"\$@\"" >> $SCRIPT
chmod +x $SCRIPT

zip -r suave-viz-$VERSION.zip $DIR
rm -rf $DIR

