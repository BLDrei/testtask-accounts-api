package com.swedbank.accounts.util;

import java.util.Objects;

public class CompareUtil {
  private CompareUtil() {}

  public static <T extends Comparable<T>> boolean lessThan(T left, T right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);

    return left.compareTo(right) < 0;
  }

  public static <T extends Comparable<T>> boolean lessThanOrEquals(T left, T right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);

    return left.compareTo(right) <= 0;
  }

  public static <T extends Comparable<T>> boolean greaterThan(T left, T right) {
    return lessThan(right, left);
  }

  public static <T extends Comparable<T>> boolean greaterThanOrEquals(T left, T right) {
    return lessThanOrEquals(right, left);
  }
}
