const express = require('express');
const axios = require('axios');

const app = express();

const JAVA_SERVICE = process.env.JAVA_SERVICE;
const PYTHON_SERVICE = process.env.PYTHON_SERVICE;

app.get('/report', async (req, res) => {

    try {

        const studentsResponse = await axios.get(
            `http://${JAVA_SERVICE}:8080/students`
        );

        const statsResponse = await axios.get(
            `http://${PYTHON_SERVICE}:5000/stats`
        );

        res.json({
            students: studentsResponse.data,
            statistics: statsResponse.data
        });

    } catch (error) {

        res.status(500).json({
            error: error.message
        });
    }
});

app.listen(3000, '0.0.0.0', () => {
    console.log('Node service running on port 3000');
});