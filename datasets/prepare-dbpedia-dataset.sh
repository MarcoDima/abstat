#! /bin/bash

set -e

version=$1
include_raw=$2
dbpedia_downloads="http://downloads.dbpedia.org/${version}"

target_directory=/schema-summaries/data/datasets/dbpedia-$1$include_raw
rm -rf $target_directory
mkdir -p $target_directory

ontology_directory=$target_directory/ontology
mkdir $ontology_directory
wget "$dbpedia_downloads/dbpedia_$version.owl.bz2" -P $ontology_directory
bunzip2 "$ontology_directory/dbpedia_$version.owl.bz2"

triples_directory=$target_directory/triples
wget "$dbpedia_downloads/en/instance_types_en.nt.bz2" -P $triples_directory
bunzip2 $triples_directory/instance_types_en.nt.bz2
wget "$dbpedia_downloads/en/mappingbased_properties_en.nt.bz2" -P $triples_directory
bunzip2 $triples_directory/mappingbased_properties_en.nt.bz2
wget "$dbpedia_downloads/en/persondata_en.nt.bz2" -P $triples_directory
bunzip2 $triples_directory/persondata_en.nt.bz2
wget "$dbpedia_downloads/en/specific_mappingbased_properties_en.nt.bz2" -P $triples_directory
bunzip2 $triples_directory/specific_mappingbased_properties_en.nt.bz2
if [[ $include_raw == --infobox ]]
then
	wget "$dbpedia_downloads/en/raw_infobox_properties_en.nt.bz2" -P $triples_directory
	bunzip2 "$triples_directory/raw_infobox_properties_en.nt.bz2"
fi

cat $triples_directory/*.nt | grep -v "# started" > $triples_directory/dataset
rm $triples_directory/*.nt
mv $triples_directory/dataset $triples_directory/dataset.nt


