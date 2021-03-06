#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/../summarization

cd $project

echo "*************** exporting vectors $@ ***************"
java -Xms256m -Xmx3g -cp .:'ontology_summarization.jar' it.unimib.disco.summarization.experiments.ExportPropertyVectors $@
echo "*************** done ***************"

