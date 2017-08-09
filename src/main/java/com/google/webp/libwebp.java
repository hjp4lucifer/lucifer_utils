package com.google.webp;

public class libwebp
{
  private static final int UNUSED = 1;
  private static int[] outputSize = { 0 };

  public static int WebPGetDecoderVersion()
  {
    return libwebpJNI.WebPGetDecoderVersion();
  }

  public static int WebPGetInfo(byte[] paramArrayOfByte, long paramLong, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
    return libwebpJNI.WebPGetInfo(paramArrayOfByte, paramLong, paramArrayOfInt1, paramArrayOfInt2);
  }

  public static byte[] WebPDecodeRGB(byte[] paramArrayOfByte, long paramLong, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
    return libwebpJNI.WebPDecodeRGB(paramArrayOfByte, paramLong, paramArrayOfInt1, paramArrayOfInt2);
  }

  public static byte[] WebPDecodeRGBA(byte[] paramArrayOfByte, long paramLong, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
    return libwebpJNI.WebPDecodeRGBA(paramArrayOfByte, paramLong, paramArrayOfInt1, paramArrayOfInt2);
  }

  public static byte[] WebPDecodeARGB(byte[] paramArrayOfByte, long paramLong, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
    return libwebpJNI.WebPDecodeARGB(paramArrayOfByte, paramLong, paramArrayOfInt1, paramArrayOfInt2);
  }

  public static byte[] WebPDecodeBGR(byte[] paramArrayOfByte, long paramLong, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
    return libwebpJNI.WebPDecodeBGR(paramArrayOfByte, paramLong, paramArrayOfInt1, paramArrayOfInt2);
  }

  public static byte[] WebPDecodeBGRA(byte[] paramArrayOfByte, long paramLong, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
    return libwebpJNI.WebPDecodeBGRA(paramArrayOfByte, paramLong, paramArrayOfInt1, paramArrayOfInt2);
  }

  public static int WebPGetEncoderVersion() {
    return libwebpJNI.WebPGetEncoderVersion();
  }

  private static byte[] wrap_WebPEncodeRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, float paramFloat) {
    return libwebpJNI.wrap_WebPEncodeRGB(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramFloat);
  }

  private static byte[] wrap_WebPEncodeBGR(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, float paramFloat) {
    return libwebpJNI.wrap_WebPEncodeBGR(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramFloat);
  }

  private static byte[] wrap_WebPEncodeRGBA(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, float paramFloat) {
    return libwebpJNI.wrap_WebPEncodeRGBA(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramFloat);
  }

  private static byte[] wrap_WebPEncodeBGRA(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, float paramFloat) {
    return libwebpJNI.wrap_WebPEncodeBGRA(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramFloat);
  }

  private static byte[] wrap_WebPEncodeLosslessRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5) {
    return libwebpJNI.wrap_WebPEncodeLosslessRGB(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5);
  }

  private static byte[] wrap_WebPEncodeLosslessBGR(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5) {
    return libwebpJNI.wrap_WebPEncodeLosslessBGR(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5);
  }

  private static byte[] wrap_WebPEncodeLosslessRGBA(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5) {
    return libwebpJNI.wrap_WebPEncodeLosslessRGBA(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5);
  }

  private static byte[] wrap_WebPEncodeLosslessBGRA(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5) {
    return libwebpJNI.wrap_WebPEncodeLosslessBGRA(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5);
  }

  public static byte[] WebPEncodeRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, float paramFloat)
  {
    return wrap_WebPEncodeRGB(paramArrayOfByte, 1, 1, outputSize, paramInt1, paramInt2, paramInt3, paramFloat);
  }

  public static byte[] WebPEncodeRGBA(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, float paramFloat)
  {
    return wrap_WebPEncodeRGBA(paramArrayOfByte, 1, 1, outputSize, paramInt1, paramInt2, paramInt3, paramFloat);
  }

  public static byte[] WebPEncodeBGR(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, float paramFloat)
  {
    return wrap_WebPEncodeBGR(paramArrayOfByte, 1, 1, outputSize, paramInt1, paramInt2, paramInt3, paramFloat);
  }

  public static byte[] WebPEncodeBGRA(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, float paramFloat)
  {
    return wrap_WebPEncodeBGRA(paramArrayOfByte, 1, 1, outputSize, paramInt1, paramInt2, paramInt3, paramFloat);
  }

  public static byte[] WebPEncodeLosslessRGB(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    return wrap_WebPEncodeLosslessRGB(paramArrayOfByte, 1, 1, outputSize, paramInt1, paramInt2, paramInt3);
  }

  public static byte[] WebPEncodeLosslessRGBA(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    return wrap_WebPEncodeLosslessRGBA(paramArrayOfByte, 1, 1, outputSize, paramInt1, paramInt2, paramInt3);
  }

  public static byte[] WebPEncodeLosslessBGR(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    return wrap_WebPEncodeLosslessBGR(paramArrayOfByte, 1, 1, outputSize, paramInt1, paramInt2, paramInt3);
  }

  public static byte[] WebPEncodeLosslessBGRA(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    return wrap_WebPEncodeLosslessBGRA(paramArrayOfByte, 1, 1, outputSize, paramInt1, paramInt2, paramInt3);
  }
}