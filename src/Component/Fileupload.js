import axios from "axios";
import React, { useState } from "react";

export default function FileUpload() {
  const [file, setFile] = useState(null);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      alert("Please choose a file first!");
      return;
    }

    const formData = new FormData();
    formData.append("file", file); // must match @RequestParam name

    try {
      const token = localStorage.getItem("jwttoken"); // optional if you have auth
      const res = await axios.post(
        "http://localhost:8080/api/classes/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert(res.data);
      console.log("Upload successful:", res.data);
    } catch (err) {
      console.error("Upload error:", err);
      alert("Upload failed");
    }
  };

  return (
    <div className="file-upload">
      <h2>📤 Upload File</h2>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
}
