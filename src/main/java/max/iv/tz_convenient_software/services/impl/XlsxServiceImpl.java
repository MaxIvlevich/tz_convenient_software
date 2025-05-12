package max.iv.tz_convenient_software.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import max.iv.tz_convenient_software.services.XlsxServiceInterface;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static max.iv.tz_convenient_software.utill.ExcelReader.readIntegersFromXlsx;

@Service
@RequiredArgsConstructor
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

    public void isCorrectFile(String path){
        File file = new File(path);
        if (!file.exists()) {
            log.error("File not found: {}", path);
            throw new IllegalArgumentException("File not found: " + path);
        }
        if (!file.isFile()) {
            log.error("Not a file: {}", path);
            throw new IllegalArgumentException("Not a file: " + path);
        }
        if (!file.canRead()) {
            log.error("Cannot read file: {}", path);
            throw new IllegalArgumentException("Cannot read file: " + path);
        }
        if (!path.toLowerCase().endsWith(".xlsx")) {
            log.error("Invalid file extension (expected .xlsx): {}", path);
            throw new IllegalArgumentException("Invalid file extension (expected .xlsx): " + path);
        }

    }
}
