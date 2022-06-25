from app import app
import os
from dotenv import load_dotenv

load_dotenv()

app.run(host=os.environ.get('HOST', '0.0.0.0'),
        port=os.environ.get('PORT', 5000),
        debug=bool(os.environ.get('DEBUG', '1')))
