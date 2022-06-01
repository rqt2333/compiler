//int a = 1;
//void main(){
//    double b = 2.35;
//    int a = 0;
//    a = pou();
//    int c = read();
//    int b = write(sfd,fds);
//    for(int i=0;i<10 ;i++){
//        if(a+2==0){
//            a = a+1;
//        }
//    }
//    write(a,b,d,fsd,xf);
//    return 0;
//}
//int b = 1;
//char c = 'd';
//int init(){
//    int a = write();
//}

//if测试，输入两个数，将其中较大的数加100输出
//int a = 1 ;
//int main(){
//
//    int result ;
//	int N = read() ;
//	int M = read() ;
//
//    if (M >= N){
//        result = M ;
//    }
//	else {
//	result = N;
//	}
//	a = result + 100 ;
//	write(a);
//}


//if and while,阶乘
//void main()
//
//{
//
//  int i,factor,n;
//
//  i=0;
//
//  n=read();
//
//  if(n<1)
//    {
//
//      factor=0;
//
//    }
//
//  else {
//
//    factor=1;
//
//  }
//
//  while(i<n)
//    {
//
//      i=i+1;
//
//      factor=factor*i;
//
//    }
//
//  write(factor);
//
//}

//for,求1到给定数的和,我没有定义运算符++，必须写出i=i+1
//int main()
//
//{
//  int i,N,sum = 0;
//  N = read();
//  for(i=1;i<=N;i++)
//{
//
//     sum = sum+i;
//    }
//
//  write(sum);
//
//}

//void main(){
//    double b = 2.35;
//    int a = 0;
//    for(i=0;i<=b ;i=i+1){
//        a = a+1;
//    }
//    write(a,b);
//    return 0;
//}

//for嵌套if,求1到给定数N以内所有奇数的和
//int main()
//
//{
//  int i,N,sum = 0;
//  N = read();
//  for(i=1;i<=N;i=i+1)
//{
//     if(i%2 == 1){
//        sum = sum+i;
//     }
//    }
//
//  write(sum);
//
//}

//双重for循环测试，求给定数以内的素数

//int main(){
//
//    int N = read() ;
//    int count=0,nprime=0,i,j;
//    for(i=2;i<=N;i=i+1) {
//       nprime = 0;
//       for(j=2;j<i;j=j+1) {
//	   if(i%j == 0) {
//	   nprime = nprime + 1;
//	   }
//       }
//       if(nprime == 0) {
//            write(i);
//            count = count + 1;
//        }
//}
//}

//while嵌套if测试，求给定数以内所有偶数的和
//int a = 1 ;
//
//int main(){
//
//    int i = 1,sum=0,N ;
//    N = read();
//
//    while(i < N){
//       if(i%2 == 0){
//           sum = sum +i;
//       }
//       i = i + 1;
//    }
//    write(sum);
//}

//if嵌套for和while,首先输入给定数N，
//输入选择1计算求1到N以内所有奇数的和
////输入2计算所有偶数的和,否则输出0
//int main()
//{
//  int i,N,sum = 0,choice=0;
//  N = read();
//  choice=read();
//  if(choice == 1) {
//      for(i=1;i<=N;i=i+1)
//      {
//         if(i%2 == 1){
//            sum = sum+i;
//         }
//      }
//  }
//  else if(choice == 2){
//      i=0;
//      while(i<N){
//      sum = sum + i;
//      i = i + 2;
//     }
//   }
//  write(sum);
//}

////定义一个函数的测试，输入一个数，结果加上7
//int a = 1 ;

//int sum(int a,int b);
//
//int main(){
//
//    int N = read() ;
//
////    a = sum(sum(3,4),N) ;
//    write(a);
//
//}

//int sum(int sum_x,int sum_y){
//
//    int result ;
//    result = sum_x + sum_y ;
//
//    return result ;
//
//}

//int main(){
//    int a = 1;
//    int b = 0;
//    return 0;
//}

int main(){
    int a = 1;
    int b = 2/0;
    if(a<b){
        a = a+1;
    }
    return 0;
}










