import { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [files, setFiles] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null); 
  const [isUploading, setIsUploading] = useState(false); 

  const fetchFiles = () => {
    fetch('http://localhost:8080/api/files')
      .then((response) => response.json())
      .then((data) => setFiles(data))
      .catch((error) => console.error("Error fetching files:", error));
  };

  useEffect(() => {
    fetchFiles();
  }, []);

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]); 
  };

  const handleUpload = (event) => {
    event.preventDefault();
    if (!selectedFile) return;

    setIsUploading(true);
    const formData = new FormData();
    formData.append("file", selectedFile); 

    fetch('http://localhost:8080/api/files/upload', { 
      method: 'POST',
      body: formData, 
    })
      .then((response) => {
        if (response.ok) {
          setSelectedFile(null); 
          document.getElementById('fileInput').value = ''; 
          fetchFiles(); 
        } else {
          alert("Upload failed.");
        }
      })
      .catch((error) => console.error("Error uploading:", error))
      .finally(() => setIsUploading(false)); 
  };

  return (
    <div className="container">
      <h1>Mini-S3 Dashboard</h1>
      <hr />

      <div className="upload-panel">
        <h2>Upload a File</h2>
        <form onSubmit={handleUpload} className="upload-form">
          <input 
            type="file" 
            id="fileInput"
            onChange={handleFileChange} 
          />
          <button 
            type="submit" 
            className="btn-primary"
            disabled={isUploading || !selectedFile}
          >
            {isUploading ? "Uploading..." : "Upload to Mini-S3"}
          </button>
        </form>
      </div>

      <h2>Your Files</h2>
      {files.length === 0 ? (
        <p>No files found in the database.</p>
      ) : (
        <ul className="file-list">
          {files.map((file) => (
            <li key={file.id} className="file-item">
              <div className="file-info">
                <h3>{file.originalFilename}</h3>
                <p>
                  <strong>Type:</strong> {file.contentType} | <strong>Size:</strong> {(file.sizeInBytes / 1024).toFixed(2)} KB
                </p>
              </div>
              
              <a 
                href={`http://localhost:8080/api/files/download/${file.storedFilename}`} 
                className="btn-download"
              >
                Download
              </a>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default App;