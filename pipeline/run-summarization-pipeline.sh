#!/bin/bash

function as_absolute(){
	echo `cd $1; pwd`
}

set -e
relative_path=`dirname $0`
current_directory=`cd $relative_path;pwd`

dataset=$1
if [ -z "$2" ]
then
	propMin="1"
	split_inference="1"
	cardinalities="1"
else
	propMin=$2
	split_inference=$3
	cardinalities=$4
fi


if [ $propMin -eq 1 ] || [ $split_inference -eq 1 ] || [ $cardinalities -eq 1 ];
then
	echo "The following additional features will be executed:"
	if [ $propMin -eq 1 ]
	then 
		echo " - pattern minimalization on properties"
	fi

	if [ $split_inference -eq 1 ]
	then 
		echo " - pattern inference and instances counting"
	fi

	if [ $cardinalities -eq 1 ]
	then 
		echo " - akp and predicate cardinality"
	fi
fi


data=$(as_absolute $current_directory/../data/datasets/$dataset)
results=$current_directory/../data/summaries/$dataset

mkdir -p $results
results=$(as_absolute $results)

echo "Running the summarization pipeline"
echo "With data from $data"
echo "Saving results in $results"

cd $current_directory
./run-summarization.sh $data $results $propMin $split_inference $cardinalities

echo "Done"

