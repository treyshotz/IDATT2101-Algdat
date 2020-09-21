#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <assert.h>


void swap(int *pInt1, int *pInt2);

int median3sort(int *list, int v, int h) {
    int m = (v + h) / 2;

    if (list[v] > list[m])
        swap(&list[v], &list[m]);

    if (list[m] > list[h]) {
        swap(&list[m], &list[h]);

        if (list[v] > list[m])
            swap(&list[v], &list[m]);
    }
    return m;
}

void swap(int *pInt1, int *pInt2) {
    int t = *pInt1;
    *pInt1 = *pInt2;
    *pInt2 = t;
}

int split(int *list, int v, int h) {
    int iv, ih;
    int m = median3sort(list, v, h);
    int dv = list[m];

    swap(&list[m], &list[h - 1]);

    for (iv = v, ih = h - 1;;) {
        while (list[++iv] < dv);
        while (list[--ih] > dv);

        if (iv >= ih) break;

        swap(&list[iv], &list[ih]);
    }
    swap(&list[iv], &list[h - 1]);
    return iv;
}

int partition(int *list, int low, int high, int *lp) {
    if (list[low] > list[high])
        swap(&list[low], &list[high]);

    int j = low + 1;
    int g = high - 1, k = low + 1, p = list[low], q = list[high];
    while (k <= g) {

        if (list[k] < p) {
            swap(&list[k], &list[j]);
            j++;
        } else if (list[k] >= q) {
            while (list[g] > q && k < g)
                g--;
            swap(&list[k], &list[g]);
            g--;
            if (list[k] < p) {
                swap(&list[k], &list[j]);
                j++;
            }
        }
        k++;
    }
    j--;
    g++;

    swap(&list[low], &list[j]);
    swap(&list[high], &list[g]);

    *lp = j;

    return g;
}

void dualPivotQuickSort(int *list, int low, int high) {
    if (low < high) {
        swap(&list[low], &list[low + (high - low) / 3]);
        swap(&list[high], &list[high - (high - low) / 3]);

        int lp, rp;
        rp = partition(list, low, high, &lp);
        dualPivotQuickSort(list, low, lp - 1);
        dualPivotQuickSort(list, lp + 1, rp - 1);
        dualPivotQuickSort(list, rp + 1, high);
    }
}

void quicksort(int *list, int v, int h) {
    if (h - v > 2) {
        int delepos = split(list, v, h);
        quicksort(list, v, delepos - 1);
        quicksort(list, delepos + 1, h);
    } else median3sort(list, v, h);
}

void randomList(int *list, int size, int random_val) {
    srand(time(0));

    for (int i = 0; i < size; i++) {
        list[i] = rand() % random_val;
    }
}

void sortedList(int *list, int size) {
    for (int i = 0; i < size; i++) {
        list[i] = i;
    }
}

int checksum(const int *list, int size) {
    int sum = 0;

    for (int i = 0; i < size; i++) {
        sum += list[i];
    }

    return sum;
}

int checkSort(const int *list, int size) {
    for (int i = 0; i < size - 1; i++) {
        assert(list[i] <= list[i + 1]);
    }
    return 1;
}

void randomListSetup(int size, int random_val) {

    int *randomArray = (int *) malloc(size * sizeof(int));
    randomList(randomArray, size, random_val);

    //Runs a checksum on the list
    int sum = checksum(randomArray, size);

    //Sort
    clock_t start, end;
    double cpu_time_used;
    start = clock();

    quicksort(randomArray, 0, size - 1);

    end = clock();
    cpu_time_used = ((double) (end - start)) / CLOCKS_PER_SEC;

    printf("Time used on random array: %f \n", cpu_time_used);


    int sum2 = checksum(randomArray, size);
    assert(sum == sum2);

    checkSort(randomArray, size);

    //Clearing memory to avoid memory leak
    free(randomArray);
}

void sortedListSetup(int size) {
    //Sorted array
    int *sortedArray = (int *) malloc(size * sizeof(int));
    sortedList(sortedArray, size);

    int sum = 0;
    clock_t start, end;
    double cpu_time_used;
    sum = checksum(sortedArray, size);

    start = clock();

    quicksort(sortedArray, 0, size - 1);

    end = clock();
    cpu_time_used = ((double) (end - start)) / CLOCKS_PER_SEC;

    printf("Time used on a sorted array: %f \n", cpu_time_used);

    int sum2 = checksum(sortedArray, size);
    assert(sum = sum2);

    checkSort(sortedArray, size);

    free(sortedArray);
}

void duplicateListSetup(int size, int random_val) {
    int *duplicateArray = (int *) malloc(size * sizeof(int));
    randomList(duplicateArray, size, random_val);

    clock_t start, end;
    double cpu_time_used;
    int sum = checksum(duplicateArray, size);

    start = clock();

    quicksort(duplicateArray, 0, size - 1);

    end = clock();

    cpu_time_used = ((double) (end - start)) / CLOCKS_PER_SEC;

    printf("Time used on list with duplicates: %f \n", cpu_time_used);

    int sum2 = checksum(duplicateArray, size);
    assert(sum == sum2);

    checkSort(duplicateArray, size);

    //Clearing memory to avoid memory leak
    free(duplicateArray);
}

void dualPivotRandomSetup(int size, int random_val) {

    int *dualRandomList = (int *) malloc(size * sizeof(int));
    randomList(dualRandomList, size, random_val);

    int sum = checksum(dualRandomList, size);

    clock_t start, end;
    double cpu_time_used;

    start = clock();

    dualPivotQuickSort(dualRandomList, 0, size - 1);

    end = clock();
    cpu_time_used = ((double) (end - start)) / CLOCKS_PER_SEC;

    printf("Times used on dual pivot random: %f \n", cpu_time_used);

    int sum2 = checksum(dualRandomList, size);
    assert(sum == sum2);

    checkSort(dualRandomList, size);

    //Clearing memory to avoid memory leak
    free(dualRandomList);
}

void dualPivotSortedSetup(int size) {
    int *dualSortedList = (int *) malloc(size * sizeof(int));
    sortedList(dualSortedList, size);

    int sum = checksum(dualSortedList, size);

    clock_t start, end;
    double cpu_time_used;

    start = clock();
    dualPivotQuickSort(dualSortedList, 0, size - 1);
    end = clock();

    cpu_time_used = ((double) (end - start)) / CLOCKS_PER_SEC;

    printf("Time used on sorted array: %f \n", cpu_time_used);

    int sum2 = checksum(dualSortedList, size);

    assert(sum == sum2);
    checkSort(dualSortedList, size);

    //Clearing memory to avoid memory leak
    free(dualSortedList);
}

void dualDuplicateSetup(int size, int random_val) {
    int *dualDuplicateList = (int *) malloc(size * sizeof(int));
    randomList(dualDuplicateList, size, random_val);

    int sum = checksum(dualDuplicateList, size);

    clock_t start, end;
    double cpu_time_used;

    start = clock();
    dualPivotQuickSort(dualDuplicateList, 0, size-1);

    end = clock();
    cpu_time_used = ((double ) (end-start)) / CLOCKS_PER_SEC;

    printf("Times used on list with duplicates: %f\n", cpu_time_used);

    int sum2 = checksum(dualDuplicateList, size);

    //For checking the sum is the same before and after sorting
    assert(sum == sum2);
    //For checking that the sort is done correct
    checkSort(dualDuplicateList, size);

    //Clearing memory to avoid memory leak
    free(dualDuplicateList);
}

int main() {

    int size = 100000000;
    int big_random_val = 10000;
    int small_random_val = 10;

    puts("Normal quicksort:");

    randomListSetup(size, big_random_val);

    sortedListSetup(size);

    duplicateListSetup(size, small_random_val);

    puts("\nDual pivot quicksort:");

    dualPivotRandomSetup(size, big_random_val);

    dualPivotSortedSetup(size);

    dualDuplicateSetup(size, small_random_val);

    return 0;
}