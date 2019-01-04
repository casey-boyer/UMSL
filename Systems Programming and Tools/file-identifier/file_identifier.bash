#!/bin/bash

function identify_file()
{

  passedFile=`echo "$1" | sed 's/\/\//\//'`

  if test -h $passedFile #If the argument passed is a symbolic link
  then
    fileName="$(echo "$passedFile" | awk -F\/ '{print $NF}')" #Get the file name as a string
    revString="$(ls -l "$passedFile" | rev)" #Reverse the path of the symbolic link file, so that the linked to file is first
    getName="$(echo $revString | cut -f1 -d'>')" #Obtain just the linked to file with the delimiter >
    linkedFileName="$(echo $getName | rev)" #Reverse getName to obtain the linked to file name

    if [ "$(echo  $passedFile | grep [/])" ]; #If the name of the file contains a '/'
    then
      parentName="$(basename "$(dirname "$passedFile")")" #Get the parent directory name
      #Change the output to contain the directory name and file name
      echo "#SYMBOLIC LINK# --> ##DIRECTORY/FILE NAME: $parentName/$fileName --> ##LINKED FILE NAME: $linkedFileName"
      echo " "
    else #If the argument did not come from a directory
      echo "#SYMBOLIC LINK# --> ##FILE NAME: $fileName --> ##LINKED FILE NAME: $linkedFileName"
      echo " "
    fi

  elif test -f "$passedFile" #If the argument passed is a regular file
  then
    fileName="$(echo "$passedFile" | awk -F\/ '{print $NF}')" #Get the file name as a string
    fileType=$(file "$passedFile" | rev | cut -f1 -d':' | rev) #Get the file type as a string
    fileSize="$(du -h "$passedFile" | cut -f1 -d'K')" #Get the file size as a string

    if [ "$(echo $passedFile | grep [/])" ]; #If the name of the file contains a '/'
    then
      parentName="$(basename "$(dirname "$passedFile")")" #Get the parent directory name
	  #Change the output to contain the directory name and file name
      echo "#REGULAR FILE# --> DIRECTORY/FILE NAME: $parentName/$fileName --> ##FILE TYPE: $fileType --> ##FILE SIZE: $fileSize kilobytes"
      echo " "
    else #If the argument did not come from a directory
      echo "#REGULAR FILE# --> ##FILE NAME: $fileName --> ##FILE TYPE: $fileType --> ##FILE SIZE: $fileSize kilobytes"
      echo " "
    fi

  elif test -d "$passedFile" #If the argument passed is a directory
  then
    for contents in "$passedFile"/* #Search all of the items in the directory
    do
      #Recurisvely call the function, individually sending each item in the directory as an argument
      if test -e "$passedFile"
      then
        identify_file $contents
      fi
    done

  else #The argument is not a regular file, symbolic link, or directory
    echo Unknown file $passedFile
  fi
}

#Iterate through each command line argument and determine if the file exists or not.
for arg in $@
do
  c=$arg

  if [ -a "$c" ];
  then #The file indeed exists
    identify_file $c
  else #The file does not exist
    echo The argument does not exist
  fi
done
