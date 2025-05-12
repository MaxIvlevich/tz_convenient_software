package max.iv.tz_convenient_software.services.impl;

import lombok.extern.slf4j.Slf4j;
import max.iv.tz_convenient_software.services.XlsxServiceInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static max.iv.tz_convenient_software.utill.ExcelReader.readIntegersFromXlsx;

@Slf4j
@Service
@Primary
public class AlternativeAlgo implements XlsxServiceInterface {
    private final Random random = new Random();
    private static final int DATA_COLUMN_INDEX = 0;
    @Override
    public int findNthMin(String path, int n) {
        log.info("Вызван метод findNthMin с path='{}', n={}", path, n);
        isCorrectFile(path);

        List<Integer> numbers = readIntegersFromXlsx(path);

        if (numbers.isEmpty() && n > 0) {
            log.error("В файле '{}' не найдено валидных целых чисел в столбце {}.", path, DATA_COLUMN_INDEX + 1);
            throw new IllegalArgumentException("Файл не содержит валидных целых чисел в указанном столбце.");
        }
        if (numbers.isEmpty()){
            log.warn("Файл '{}' не содержит валидных целых чисел в столбце {}. Возврат невозможен для n={}", path, DATA_COLUMN_INDEX + 1, n);
            throw new IllegalArgumentException("Файл не содержит валидных целых чисел, невозможно найти N-ный минимум.");
        }

        return findNthMinimum(numbers, n);
    }


    @Override
    public int findNthMinimum(List<Integer> numbers, int n) {
        if (numbers == null) {
            log.error("Некорректные входные данные для findNthMinimum: список равен null, n={}", n);
            throw new IllegalArgumentException("Входной список не может быть null.");
        }

        List<Integer> nonNullNumbers = numbers.stream()
                .filter(Objects::nonNull)
                .toList();

        int effectiveSize = nonNullNumbers.size();

        if (n <= 0 || effectiveSize < n) {
            log.error("Некорректные входные данные для findNthMinimum: эффективный размер списка={}, n={}. Исходный размер списка был {}.",
                    effectiveSize, n, numbers.size());
            throw new IllegalArgumentException("Некорректные входные данные: N должно быть положительным ("
                    + n + ") и не должно превышать количество не-null элементов в списке (" + effectiveSize + ").");
        }

        int result = quickSelect(new ArrayList<>(nonNullNumbers), 0, effectiveSize - 1, n - 1);
        log.info("{}-е минимальное число: {}", n, result);
        return result;
    }



    public void isCorrectFile(String pathString) {
        log.debug("Проверка файла: {}", pathString);
        try {
            Path path = Paths.get(pathString);

            if (!Files.exists(path)) {
                log.error("Файл не найден: {}", pathString);
                throw new IllegalArgumentException("Файл не найден: " + pathString);
            }
            if (!Files.isRegularFile(path)) {
                log.error("Путь не указывает на файл: {}", pathString);
                throw new IllegalArgumentException("Путь не указывает на файл: " + pathString);
            }
            if (!Files.isReadable(path)) {
                log.error("Нет прав на чтение файла: {}", pathString);
                throw new IllegalArgumentException("Нет прав на чтение файла: " + pathString);
            }
            String fileName = path.getFileName().toString();
            if (!fileName.toLowerCase().endsWith(".xlsx")) {
                log.error("Некорректное расширение файла (ожидается .xlsx): {}", pathString);
                throw new IllegalArgumentException("Некорректное расширение файла (ожидается .xlsx): " + pathString);
            }
            log.debug("Файл '{}' прошел проверку.", pathString);
        } catch (InvalidPathException e) {
            log.error("Некорректный формат пути: {}", pathString, e);
            throw new IllegalArgumentException("Некорректный формат пути: " + pathString, e);
        }
    }

    private int quickSelect(List<Integer> list, int left, int right, int k) {
        while (left <= right) {
            if (left == right) {
                return list.get(left);
            }

            int pivotIndex = random.nextInt(right - left + 1) + left;
            pivotIndex = partition(list, left, right, pivotIndex);

            if (k == pivotIndex) {
                return list.get(k);
            } else if (k < pivotIndex) {
                right = pivotIndex - 1;
            } else {
                left = pivotIndex + 1;
            }
        }
        throw new IllegalStateException("Ошибка алгоритма Quickselect: не удалось найти элемент.");
    }

    private int partition(List<Integer> list, int left, int right, int pivotIndex) {
        int pivotValue = list.get(pivotIndex);
        swap(list, pivotIndex, right);
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (list.get(i) < pivotValue) {
                swap(list, storeIndex, i);
                storeIndex++;
            }
        }
        swap(list, storeIndex, right);
        return storeIndex;
    }

    private void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}

