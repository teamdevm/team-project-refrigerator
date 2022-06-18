from flask import Flask
import os

app = Flask(__name__)


def run():
    app.run(host=os.environ.get('HOST', '0.0.0.0'),
            port=os.environ.get('PORT', 5000),
            debug=bool(os.environ.get('DEBUG', '1')))