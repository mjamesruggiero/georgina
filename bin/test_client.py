#!/usr/bin/env python
import csv
import json
import logging
import os
import requests
import sys
import time
logging.basicConfig(level=logging.INFO, format="%(lineno)d\t%(message)s")


def post_to_client(payload, endpoint='http://localhost:8080/transactions/'):
    payload_as_json = json.dumps(payload)
    headers = {'content-type': 'application/json'}
    logging.debug("payload is {p}".format(p=payload_as_json))
    r = requests.post(endpoint, data=payload_as_json, headers=headers)
    logging.info("response: {0}".format(r.status_code))


def transaction_posts(csv_filepath):
    f = open(filepath, 'rU')

    fields = ('date', 'amount', 'asterisk', 'check', 'description')
    reader = csv.DictReader(f, fieldnames=fields)
    raw_csv_rows = [row for row in reader]
    formatted_rows = []
    for row in raw_csv_rows:
        row = reformat_row(row)
        formatted_rows.append(row)
    return formatted_rows


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


def main(filepath, number=0):
    posts = transaction_posts(filepath)
    if number > 0:
        posts = posts[:number]
    for post in posts:
        time.sleep(1)
        post_to_client(post)

if __name__ == '__main__':
    try:
        filepath = sys.argv[1]
        LIMIT = 0
        main(filepath, LIMIT)
    except Exception, err:
        logging.error("error is {e}".format(e=err))
        print "usage: {0} <filepath>".format(os.path.basename(sys.argv[0]))
        sys.exit(1)
