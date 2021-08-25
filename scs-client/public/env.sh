#!/bin/bash

cp /usr/share/nginx/html/properties.json temp-properties.json


for var in "${!SCS_@}"; do
    printf '%s = %s\n' "$var" "${!var}"
    sed -i "s/\${$var}/${!var}/g" temp-properties.json
done


mv temp-properties.json /usr/share/nginx/html/properties.json


