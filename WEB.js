function openModal(img) {
    const modal = document.getElementById("modal");
    const modalImg = document.getElementById("modal-img");
    const modalTitle = document.getElementById("infopic");
    const modalInfo = document.getElementById("morinfo");

    modal.style.display = "block";
    modalImg.src = img.src;
    modalTitle.textContent = img.getAttribute("data-title") || "";

    const info = img.getAttribute("data-info") || "";
    modalInfo.innerHTML = info.replace(/\n/g, "<br>");
}

function closeModal() {
    document.getElementById("modal").style.display = "none";
}
