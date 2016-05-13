#!bin/bash

TOP_PATH=`pwd`
LIB_PATH=${TOP_PATH}/utility

cd tools

tar zxvf curl-7.48.0.tar.gz
cd curl-7.48.0
./configure --prefix=${LIB_PATH}/curl
make
make install
cd ..

rm -rf curl-7.48.0

cd ..

make