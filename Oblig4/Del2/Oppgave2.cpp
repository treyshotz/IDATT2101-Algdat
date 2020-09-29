#include <iostream>
#include <chrono>
#include <map>

using namespace std;
const int size = 10000000;
const int pow2to24 = 16777216U;
int randomArr[size];
int *hashMap[pow2to24];
int collisions = 0;

//Generates first hash
unsigned long hashOne(int num) {
    return (unsigned)num * (unsigned)2654435769 >> (32-24);
}


unsigned long hashTwo(int num) {
    return (2*num+1)%pow2to24;
}


unsigned insertNum(int *randomNum, int *hashmap[]) {
        unsigned long pos = hashOne(*randomNum);
        if(hashmap[pos] == nullptr) {
            hashmap[pos] = randomNum;
        } else {
            collisions++;
            int jump = hashTwo(*randomNum);
            pos = (pos+jump)%pow2to24;
            while(hashmap[pos] != NULL) {
                collisions++;
                pos = (pos+jump)%pow2to24;
            }
            hashmap[pos] = randomNum;
        }
        return 1;
}


int main() {
    //Setting up for generating random numbers
    srand(time(NULL));

    for(int i = 0; i < size; i++) {
        randomArr[i] = (int) rand();
    }

    //Take time here
    chrono::steady_clock::time_point beginning = chrono::steady_clock::now();

    for(int i = 0; i < size; i++) {
        insertNum(&randomArr[i], hashMap);
    }

    chrono::steady_clock::time_point end = chrono::steady_clock::now();

    cout << "Collisions: " << collisions << endl;
    cout << "Time used: " << chrono::duration_cast<chrono::milliseconds>(end - beginning).count() << "ms" << endl;
    cout << "Load: " << (double) size/pow2to24 << endl;
    cout << "Collisions per element: " << (double) collisions/size << endl;

    map<int, int> map;

    chrono::steady_clock::time_point beginning2 = chrono::steady_clock::now();

    for(int i = 0; i < size; i++) {
        map[randomArr[i]] = randomArr[i];
    }
    chrono::steady_clock::time_point end2 = chrono::steady_clock::now();

    cout << "Time used for c++ hashmap: " << chrono::duration_cast<chrono::milliseconds>(end2-beginning2).count() << "ms" << endl;

}
