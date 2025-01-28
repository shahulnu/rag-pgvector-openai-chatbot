package com.springai.rag_pgvector_openai_chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class RagPgvectorOpenaiChatbotApplication implements CommandLineRunner {

	private final PdfFileReader pdfFileReader;
	private final TikaFileReader tikaFileReader;

	@Value("classpath:Demo.pdf")
	private Resource pdfResource;

	@Value("classpath:Demo.csv")
	private Resource csvResource;

	public RagPgvectorOpenaiChatbotApplication(PdfFileReader pdfFileReader, TikaFileReader tikaFileReader) {
        this.tikaFileReader = tikaFileReader;
        this.pdfFileReader = pdfFileReader;
    }

	@Override
    public void run(String... args) {
		log.info("Processing PDF file");
        pdfFileReader.addResource(pdfResource);

		log.info("Processing CSV file");
        tikaFileReader.addResource(csvResource);
    }

	public static void main(String[] args) {
		SpringApplication.run(RagPgvectorOpenaiChatbotApplication.class, args);
	}

}
