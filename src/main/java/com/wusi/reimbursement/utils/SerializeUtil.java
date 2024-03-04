package com.wusi.reimbursement.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
    public SerializeUtil() {
    }

    public static byte[] serialize(Object value) {
        if (value == null) {
            throw new NullPointerException("Can't serialize null");
        } else {
            byte[] rv = null;
            ByteArrayOutputStream bos = null;
            ObjectOutputStream os = null;

            try {
                bos = new ByteArrayOutputStream();
                os = new ObjectOutputStream(bos);
                os.writeObject(value);
                os.close();
                bos.close();
                rv = bos.toByteArray();
            } catch (Exception var8) {
                var8.printStackTrace();
                System.out.println("serialize error");
            } finally {
                close(os);
                close(bos);
            }

            return rv;
        }
    }

    public static Object deserialize(byte[] in) {
        return deserialize(in, Object.class);
    }

    public static Object deserialize(byte[] in, Object requiredType) {
        Object rv = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;

        try {
            if (in != null) {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                rv = is.readObject();
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            System.out.println("deserialize error");
        } finally {
            close(is);
            close(bis);
        }

        return rv;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
                var2.printStackTrace();
                System.out.println("close stream error");
            }
        }

    }

}
