import unittest
from app import app

class FlaskAppTestCase(unittest.TestCase):

    def setUp(self):
        # Set up the test client
        self.testing = True
        self.client = app.test_client()

    def test_home_route(self):
        # Send a GET request to the home page
        response = self.client.get('/')

        # Check that the response status code is 200 (OK)
        self.assertEqual(response.status_code, 200)

        # Check that the response data is as expected
        self.assertIn(b"Welcome to the Flask App!", response.data)

# Run the tests
unittest.main(argv=[''], verbosity=2, exit=False)