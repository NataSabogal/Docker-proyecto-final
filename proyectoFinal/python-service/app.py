from flask import Flask, jsonify
import mysql.connector
import os
import time

app = Flask(__name__)

DB_HOST = os.getenv("DB_HOST")
DB_PORT = os.getenv("DB_PORT")
DB_NAME = os.getenv("DB_NAME")
DB_USER = os.getenv("DB_USER")
DB_PASSWORD = os.getenv("DB_PASSWORD")


def get_connection():

    retries = 10

    while retries > 0:

        try:

            conn = mysql.connector.connect(
                host=DB_HOST,
                port=DB_PORT,
                user=DB_USER,
                password=DB_PASSWORD,
                database=DB_NAME
            )

            return conn

        except Exception as e:

            print("Waiting for MySQL...")
            print(e)

            retries -= 1
            time.sleep(5)

    raise Exception("MySQL is not available")


@app.route('/stats')
def stats():

    conn = get_connection()

    cursor = conn.cursor(dictionary=True)

    cursor.execute(
        "SELECT COUNT(*) AS total_students FROM students"
    )

    total = cursor.fetchone()

    cursor.execute(
        "SELECT AVG(age) AS average_age FROM students"
    )

    average = cursor.fetchone()

    cursor.close()
    conn.close()

    return jsonify({
        "total_students": total["total_students"],
        "average_age": round(float(average["average_age"]), 2)
    })


if __name__ == '__main__':

    app.run(
        host='0.0.0.0',
        port=5000
    )