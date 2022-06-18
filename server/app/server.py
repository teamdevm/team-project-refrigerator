from flask import Flask, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow 
import os

app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get('DB_URI')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)
ma = Marshmallow(app)

class Product (db.Model):
    __tablename__ = 'server_products'

    barcode = db.Column(db.String(48), primary_key = True)
    product_name = db.Column(db.Text)
    category_id = db.Column(db.Integer, db.ForeignKey('category.id'))
    protein = db.Column(db.Float)
    fat = db.Column(db.Float)
    carbohydrate = db.Column(db.Float)
    energy = db.Column(db.Integer)
    photo = db.Column(db.Text)

    def __init__(self, barcode, product_name, category_id, protein, fat, carbohydrate, energy, photo):
        self.barcode = barcode
        self.product_name = product_name
        self.category_id = category_id  
        self.protein = protein
        self.fat = fat
        self.carbohydrate = carbohydrate
        self.energy = energy
        self.photo = photo


class Category (db.Model):
    __tablename__ = 'category'

    id = db.Column(db.Integer, primary_key = True)
    name = db.Column(db.String(50))
    products = db.relationship('Product', backref='category', lazy = 'dynamic')

    def __init__(self, id,  name):
        self.id = id
        self.name = name




def run():
    app.run(host=os.environ.get('HOST', '0.0.0.0'),
            port=os.environ.get('PORT', 5000),
            debug=bool(os.environ.get('DEBUG', '1')))