from flask import jsonify
from app import app
from app.schemes import ProductSchema, CategorySchema
from app.models import Product, Category

product_schema = ProductSchema()
products_schema = ProductSchema(many=True)

category_schema = CategorySchema()
categories_schema = CategorySchema(many=True)

@app.route('/')
def hello():
    return 'Всем привет)'
