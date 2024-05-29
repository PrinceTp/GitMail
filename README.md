# GitMail

GitMail is a Java-based application designed to manage emails within a simulated environment. It utilizes Spring Boot for its framework, providing a robust platform for web applications. GitMail is built using Java, Spring Boot and Apache Cassandra.
It is a full web application that is scalable and highly available. 
The idea is to make use of GitHub ID of the logged in user to send and recieve mail/messages.

## Features

- **Email Management**: Send and receive emails.
- **Folder Organization**: Organize emails into folders like Work, Home, and Uni.
- **Predefined Users**: Initial setup includes predefined user configurations.

## Technologies Used

- **Application Tier**: Spring Boot
- **Database**: Apache Cassandra
- **Data Layer**: Spring Data Cassandra
- **Security**: Spring Security
- **View Layer**: Thymeleaf

https://github.com/PrinceTp/GitMail/assets/104999853/a0d5ff0c-5d7d-4bd4-8b67-778dcc3a5835

## Installation

To run GitMail, you need to have Java and Maven installed on your system.

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repository/GitMail.git
   ```
2. **Navigate to the project directory**:
   ```bash
   cd GitMail
   ```
3. **Build the project**:
   ```bash
   mvn clean install
   ```
4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

## Configuration

Before running the application, ensure you have the necessary configuration for connecting to DataStax Astra DB by setting up the `DataStaxAstraProperties` to include your secure connect bundle.

## Usage

Once the application is running, it will automatically set up initial folders and send some random emails to the user "PrinceTp". You can modify the source code to customize the initial setup or to add additional functionalities.

## Contributing

Contributions are welcome. Please fork the repository and submit a pull request with your features or fixes.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
