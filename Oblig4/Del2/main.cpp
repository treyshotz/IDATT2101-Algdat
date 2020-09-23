#include <iostream>
#include <chrono>

using namespace std;

    const int size = 10000000;
    const int pow2to24 = 16777216;
    const unsigned int A = 2654435769;
    int randomArr[1000000];
    int *hashMap[pow2to24];
//int hashmap[pow2to24];
    int collisions = 0;


unsigned long hashOne(int num) {
    return (unsigned)num * (unsigned)2654435769 >> (32-24);
}


unsigned long hashTwo(int num) {
    return (2*num+1)%pow2to24;
}


unsigned long insertNum(int *randomNum, int *hashMap[]) {
        unsigned long pos = hashOne(*randomNum);
        //cout << hashMap[pos] << endl;
        if(hashMap[pos] == NULL) {
            hashMap[pos] = randomNum;
        } else {
            collisions++;
            int jump = hashTwo(*randomNum);
            pos = (pos+jump)%pow2to24;
            while(hashMap[pos] != NULL) {
                collisions++;
                //cout << collisions << endl;
                pos = (pos+jump)%pow2to24;
            }
            hashMap[pos] = randomNum;
        }
        return 1;
    }



int main() {
    //Setting up for generating random numbers
    srand(time(NULL));

    for(int i = 0; i < size; i++) {
        randomArr[i] = (int) rand();
    }

    chrono::steady_clock::time_point beginning =chrono::steady_clock::now();

    //Ta tid her
    for(int i = 0; i < size; i++) {
        insertNum(&randomArr[i], (hashMap));
    }

    chrono::steady_clock::time_point end =chrono::steady_clock::now();

    cout <<"Collisions: " << collisions << endl;
    cout << "Time used = " << chrono::duration_cast<chrono::milliseconds>(end - beginning).count() << "ms" << endl;
}
