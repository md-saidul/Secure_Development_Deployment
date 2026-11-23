import unittest
from maths import add


# Create a test case class
class TestMathOperations(unittest.TestCase):

    # Define a test method
    def test_add(self):
        self.assertEqual(add(1, 2), 3)
        self.assertEqual(add(-1, 1), 0)
        self.assertEqual(add(0, 0), 0)

unittest.main(argv=[''], verbosity=5, exit=False)