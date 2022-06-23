from app import db

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
    expiring_date = db.Column(db.Integer)

    def __init__(self, barcode, product_name, category_id, protein, fat, carbohydrate, energy, photo, expiring_date):
        self.barcode = barcode
        self.product_name = product_name
        self.category_id = category_id  
        self.protein = protein
        self.fat = fat
        self.carbohydrate = carbohydrate
        self.energy = energy
        self.photo = photo
        self.expiring_date = expiring_date

class Category (db.Model):
    __tablename__ = 'category'

    id = db.Column(db.Integer, primary_key = True)
    name = db.Column(db.String(50))
    products = db.relationship('Product', backref='category', lazy = 'dynamic')

    def __init__(self, id,  name):
        self.id = id
        self.name = name

