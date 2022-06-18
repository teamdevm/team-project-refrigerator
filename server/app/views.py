from flask import jsonify, request
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

@app.route('/products', methods = ['GET'])
def get_products_list():
    list_of_barcodes = request.args.get('list_of_barcodes')
    if not list_of_barcodes:
      products = Product.query.all()
    else:
      products = Product.query.filter(Product.barcode.in_(list_of_barcodes.split(','))).all()
    result = products_schema.dump(products)
    return jsonify(result)
      

@app.route('/products/<barcode>', methods = ['GET'])
def get_product(barcode):
    product = Product.query.filter(Product.barcode == barcode).first()

    if not product:
        return 'Не удалось найти продукт с данных штрих-кодом', 404

    result = product_schema.dump(product)
    return jsonify(result)


@app.route('/products/category/<id>', methods = ['GET'])
def get_products_by_id(id):
    category = Category.query.filter(Category.id == id).first()

    if not category:
      return 'Не удалось найти категорию с данным id', 404

    result = products_schema.dump(category.products)
    return jsonify(result)

