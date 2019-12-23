#!/usr/bin/env bash

IN="""
  com.haulmont.addon.sdbmt:sdbmt-global:1.4.0
  com.haulmont.addon.dashboard:dashboard-global:3.1.1
"""

for i in $(echo $IN | tr "\n" "\n"); do
  (echo "add-component"; echo "$i") | cuba-cli
done