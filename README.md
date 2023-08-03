# Auction Management System

The Auction Management System is a web-based platform designed to facilitate online auctions for various items and products. The system allows users to create lots, place bids, and monitor the auction process in real-time. It provides a user-friendly interface for both auction organizers and bidders to participate in auctions seamlessly.

---

### Key Features:

Lot Creation: Users can create lots for auction by providing essential details such as title, description, start price, and bid price.

Real-Time Bidding: Bidders can place their bids on lots, and the system updates the current price and last bidder in real-time.

Auction Status Management: The system handles the lifecycle of auctions, enabling auction organizers to start, stop, and track the status of ongoing auctions.

Bid Analytics: The system provides analytics on bidding activity, including the most frequent bidder for a specific lot and the first bidder.

CSV Export: Auction organizers can export auction data to CSV format, which includes lot information such as ID, title, status, last bidder, and current price.

---

### Instructions for Running the Project:

1. Clone the repository from GitHub Repository .
2. Configure the PostgreSQL database connection in the "application.properties" file.
3. Run the application using the "AuctionApplication" class as the main class.
4. Access the application at [http://localhost:8080/].

---

### Technology Stack:

1. Backend: Java, Spring Boot, Hibernate, Liquibase
2. Database: PostgreSQL
3. Additional: Lombok for code simplification, Apache Commons CSV for CSV data export.

---

### Contributions:

The Auction Management System is an ongoing project, and contributions from developers and designers are welcome. If you are interested in contributing or have ideas for improvement, feel free to submit pull requests or raise issues on the GitHub repository.
