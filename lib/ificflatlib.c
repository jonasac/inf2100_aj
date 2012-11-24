#include <stdio.h>

double getdouble (void)
{
  double x;
  scanf("%lf", &x);
  return x;
}

int getint (void)
{
  int x;
  scanf("%d", &x);
  return x;
}

double putdouble (double x)
{
  printf("%lf", x);
  return x;
}

int putint (int x)
{
  printf("%d", x);
  return x;
}
