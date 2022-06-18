from app import ma

class ProductSchema(ma.Schema):
  class Meta:
    fields = ('barcode', 'product_name', 'category_id', 'protein', 'fat', 'carbohydrate', 'energy', 'photo')

class CategorySchema(ma.Schema):
  class Meta:
    fields = ('id', 'name')
