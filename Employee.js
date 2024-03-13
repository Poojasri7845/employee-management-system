function submitForm() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const department = document.getElementById('department').value;
    const salary = document.getElementById('salary').value;

    fetch('addEmployee', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name, email, phone, department, salary })
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById('output').innerText = data.message;
    })
    .catch(error => console.error('Error:', error));
}
