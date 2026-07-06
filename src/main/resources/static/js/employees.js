/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {

    const previewBtn = document.getElementById("previewBtn");

    previewBtn.onclick = async function() {

        const fileInput = document.getElementById("excelFile");

        const formData = new FormData();
        formData.append("file", fileInput.files[0]);

        const response = await fetch("/employees/preview", {
            method: "POST",
            body: formData
        });

        const employees = await response.json();

        const tbody = document.querySelector("#previewTable tbody");
        tbody.innerHTML = "";

        employees.forEach(emp => {
            tbody.innerHTML += `
                <tr>
                    <td>${emp.name}</td>
                    <td>${emp.email}</td>
                    <td>${emp.password}</td>
                    <td>${emp.role ?? "-"}</td>
                </tr>
            `;
        });
    };

});
function openImportModal() {
    document.getElementById("importModal").style.display = "flex";
}
function closeImportModal() {
    document.getElementById("importModal").style.display = "none";
}
