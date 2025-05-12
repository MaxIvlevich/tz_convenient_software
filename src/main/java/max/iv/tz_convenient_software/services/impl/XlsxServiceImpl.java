package max.iv.tz_convenient_software.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import max.iv.tz_convenient_software.services.XlsxServiceInterface;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static max.iv.tz_convenient_software.utill.ExcelReader.readIntegersFromXlsx;

@Service
@Slf4j
public class XlsxServiceImpl implements XlsxServiceInterface {
    @Override
    public int findNthMin(String path, int n) {
        log.info("findNthMin called with path='{}', n={}", path, n);
        isCorrectFile(path);
        List<Integer> numbers = readIntegersFromXlsx(path);
        if (numbers.isEmpty()) {
            log.error("No numbers found in the file: {}", path);
            throw new IllegalArgumentException("The file contains no numbers.");
        }

        return findNthMinimum(numbers, n);
    }
    @Override
    public int findNthMinimum(List<Integer> numbers, int n) {
        if (numbers == null || numbers.size() < n || n <= 0) {
            log.error("Invalid input for findNthMinimum: list size={}, n={}", numbers == null ? "null" : numbers.size(), n);
            throw new IllegalArgumentException("Invalid input data: the list size is less than N or N < 0");
        }
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(n, Comparator.reverseOrder());
        for (Integer number : numbers) {
            if (number == null) continue;

            if (maxHeap.size() < n) {
                maxHeap.offer(number);
            } else if (number < maxHeap.peek()) {
                maxHeap.poll();
                maxHeap.offer(number);
            }
        }
        Integer result = maxHeap.peek();
        if (result == null) {

            throw new IllegalStateException("Could not determine the nth minimum element.");
        }
        log.info("The {}-th minimum number is {}", n, result);
        return result;
    }

    public void isCorrectFile(String pathString){
        try {
            Path path = Paths.get(pathString);

            if (!Files.exists(path)) {
                log.error("File not found: {}", pathString);
                throw new IllegalArgumentException("File not found: " + pathString);
            }
            if (!Files.isRegularFile(path)) {
                log.error("Not a file: {}", pathString);
                throw new IllegalArgumentException("Not a file: " + pathString);
            }
            if (!Files.isReadable(path)) {
                log.error("Cannot read file: {}", pathString);
                throw new IllegalArgumentException("Cannot read file: " + pathString);
            }
            String fileName = path.getFileName().toString();
            if (!fileName.toLowerCase().endsWith(".xlsx")) {
                log.error("Invalid file extension (expected .xlsx): {}", pathString);
                throw new IllegalArgumentException("Invalid file extension (expected .xlsx): " + pathString);
            }
        } catch (InvalidPathException e) {
            log.error("Invalid path format: {}", pathString, e);
            throw new IllegalArgumentException("Invalid path format: " + pathString, e);
        }

    }
}
