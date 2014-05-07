#!/usr/bin/env python
import os
import sys
import csv
import json
import requests


def post_to_client(payload, endpoint='http://localhost:8080/submit'):
    headers = {'content-type': 'application/json'}
    #print "payload is {p}".format(p=payload)
    r = requests.post(endpoint, data=payload, headers=headers)
    print "r is {0}".format(r.text)


def main(csv_filepath):
    f = open(filepath, 'rU')

    fields = ('date', 'amount', 'asterisk', 'check', 'description')
    reader = csv.DictReader(f, fieldnames=fields)
    raw_csv_rows = [row for row in reader]
    formatted_rows = []
    for row in raw_csv_rows:
        row = reformat_row(row)
        formatted_rows.append(row)

    final_structure = {'transactions': formatted_rows}

    return json.dumps(final_structure)


def reformat_date(date_string):
    (month, day, year) = date_string.split('/')
    return "{0}-{1}-{2}".format(year, month, day)


def reformat_row(row):
    new_row = {}
    new_row['id'] = 0
    new_row['description'] = row['description']
    new_row['date'] = reformat_date(row['date'])
    new_row['category'] = 'unknown'
    new_row['amount'] = float(row['amount'])
    return new_row

if __name__ == '__main__':
    try:
        filepath = sys.argv[1]
        data = main(filepath)
        post_to_client(data)
    except Exception, err:
        print "error is {e}".format(e=err)
        print "usage: {0} <filepath>".format(os.path.basename(sys.argv[0]))
        sys.exit(1)
