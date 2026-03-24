# Mini-S3 Object Storage

A full-stack, enterprise-grade file storage application that mimics the core architecture of AWS S3. This project is built to handle massive file sizes efficiently across the network without overloading server memory.

## 🚀 Key Features

* **Multipart Chunked Uploads:** Bypasses standard network limitations by utilizing the browser's File API to mathematically slice large files into smaller chunks (e.g., 5MB) on the client side before transmission.
* **Resilient 3-Step Handshake API:** Implements a custom REST architecture (`/init`, `/upload`, `/complete`) to manage stateful upload sessions, ensuring chunks are routed to isolated staging directories.
* **Memory-Safe File Assembly:** Utilizes Java NIO `OutputStream` to stream and stitch binary chunks directly onto the physical disk. This prevents `OutOfMemoryError` crashes by ensuring the server RAM only processes a few megabytes at a time, regardless of the total file size.
* **Modern React Dashboard:** A fast, responsive UI built with Vite. Features a centralized dark-mode theme, dynamic state management, and real-time interaction for uploading, downloading, and deleting files.

## 🛠️ Tech Stack

* **Frontend:** React, Vite, JavaScript, Custom CSS Variables
* **Backend:** Java, Spring Boot, Spring Web
* **Database & Storage:** PostgreSQL (File Metadata), APFS/Local File System (Binary Object Storage)

## 🧠 System Architecture

1. **Initiate (`POST /init`):** The React client requests a secure `uploadId`. Spring Boot generates a UUID and spins up an isolated temporary directory on the hard drive.
2. **Stream (`POST /upload`):** React loops through the file, appending each binary chunk to a `FormData` payload. Spring Boot catches and stores these chunks sequentially.
3. **Stitch & Clean (`POST /complete`):** Upon completion, Java opens a direct file pipe in `APPEND` mode to merge the chunks, saves the finalized metadata to PostgreSQL, and recursively deletes the temporary staging environment to prevent storage leaks.

## 🏃‍♂️ How to Run Locally

### Backend Setup
1. Ensure PostgreSQL is running and your `application.properties` credentials are correct.
2. Run the Spring Boot application (defaults to `http://localhost:8080`).
3. The server will automatically generate an `uploads/` directory at the project root.

### Frontend Setup
1. Navigate to the frontend directory.
2. Run `npm install` to grab dependencies.
3. Run `npm run dev` to start the Vite development server.
