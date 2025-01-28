package com.springai.rag_pgvector_openai_chatbot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TikaFileReader {
    private final VectorStore vectorStore;

    public TikaFileReader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void processFilesInDirectory(String directoryPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile).forEach(this::addResource);
        }

    }

    public void addResource(Path path) {
        FileSystemResource resource = new FileSystemResource(path.toFile());
        TikaDocumentReader pdfDocumentReaderConfig = new TikaDocumentReader(resource);
        TokenTextSplitter textSplitter = new TokenTextSplitter();
        vectorStore.add(textSplitter.apply(pdfDocumentReaderConfig.get()));
        log.info("Uploaded file: " + resource.getFilename());
    }

    public void addResource(Resource resource) {
        TikaDocumentReader tikaDocumentReaderConfig = new TikaDocumentReader(resource);
        TokenTextSplitter textSplitter = new TokenTextSplitter();
        vectorStore.add(textSplitter.apply(tikaDocumentReaderConfig.get()));
        log.info("Uploaded file: " + resource.getFilename());
    }
}
