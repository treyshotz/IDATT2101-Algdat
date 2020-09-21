#include <stdio.h>
#include <stdlib.h>
#include <string.h>


typedef struct node Node;

struct node {
    int number;
    struct node *prev;
    struct node *next;
};

void createHead(int number, Node *headpntr);

void addNewNodeLast(int number, Node *headpntr) {

    if (headpntr->next != NULL) {
        printf("Head num %d \n", headpntr->number);
        Node *newNode = (Node *) malloc(sizeof(Node));

        newNode->number = number;
        newNode->prev = headpntr->prev;
        newNode->next = NULL;

        Node *temp = headpntr;
        while (temp->next != NULL) {
            temp = temp->next;
        }
        temp->next = newNode;

        newNode->prev = temp;

    } else {
        createHead(number, headpntr);
    }
}

void addNewNodeFirst(int number, Node *headpntr) {
    if (headpntr->next != NULL) {
        Node *newNode = (Node *) (malloc(sizeof(Node)));

        newNode->number = number;
        newNode->next = headpntr;
        newNode->prev = NULL;

        headpntr->prev = newNode;
    } else {
        createHead(number, headpntr);
    }
}

//Create new headd if it is first node added
void createHead(int number, Node *headpntr) {
    //Node *head = (Node *) malloc(sizeof(Node));

    headpntr->number = number;
    headpntr->prev = NULL;
    headpntr->next = NULL;
}

/**
 * Displays the content of the list from beginning to end
 */
void displayFirstListFromFirst(Node *headpntr) {
    struct node *temp;
    int n = 1;

    if (headpntr->next == NULL) {
        printf("List NO.1 is empty.\n");
    } else {
        temp = headpntr->next;
        printf("\n\nDATA IN LIST NO.1:\n");

        while (temp != NULL) {
            printf("DATA of %d node = %d\n", n, temp->number);

            n++;

            /* Move the current pointer to next node */
            temp = temp->next;
        }
    }
}

void cleanupList(Node *headpntr) {
    int isNull = 0;
    struct node *temp;
    while (headpntr->next != NULL && isNull == 0) {
        temp = headpntr->next;
        if (temp->number != 0) {
            isNull = 1;
            headpntr = temp;
        }
    }
}

static void destroyNode(Node *node) {
    if (node->next != NULL) {
        destroyNode(node->next);
    }

    free(node->next);

}

//static void destroyNode2(Node *endpoint) {
//    if (endpoint->last) {
//        free(endpoint->last);
//    }
//    if (endpoint->head->next != NULL) {
//        destroyNode(endpoint->head->next);
//    }
//
//    free(endpoint->head->next);
//}

int *intToArray(int num) {
    int *arr = malloc(16 * sizeof(int));
    for (int i = 16; i >= 0; i--) {
        arr[i - 1] = num % 10;
        num /= 10;
    }
    return arr;
}

int main(int argc, char *argv[]) {
    Node *node1 = (Node * ) malloc(sizeof(Node));
    Node *node2 = (Node * ) malloc(sizeof(Node));
    //Endpoint *endpoint1 = (Endpoint * ) malloc(sizeof(Endpoint));
    //Endpoint *endpoint2 = (Endpoint * ) malloc(sizeof(Endpoint));


    printf("Antall argumenter: %d \n", argc - 1);

    for (int i = 1; i < argc; i++) {
        printf("Argument %d er %s \n", i, argv[i]);
    }


    int *arr = malloc(16 * sizeof(int));
    int *arr2 = malloc(16 * sizeof(int));
    arr2 = intToArray(atoi(argv[3]));

    //arr = intToArray(atoi(argv[1]));

    //Checks if user want addition og subtraction
    char *math = "+";
    int addition = 0;

    if (strcmp(math, argv[2]) == 0) {
        addition = 1;
    }
//
//    for (int i = 0; i < 16; i++) {
//        printf("plass nr %d er %d \n", i+1, arr2[i]);
//    }
    for (int i = 0; i < 16; i++) {
        addNewNodeLast(arr2[i], node1);
    }

    //cleanupList(endpoint1);
    displayFirstListFromFirst(node1);
    //destroyNode2(endpoint1);



    return 0;
}
