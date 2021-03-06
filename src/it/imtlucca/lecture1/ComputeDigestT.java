package it.imtlucca.lecture1;

import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class ComputeDigestT extends Thread {

    private String filename;

    public ComputeDigestT(String filename){
        this.filename = filename;
    }

    @Override
    public void run(){
        try{
            FileInputStream in = new FileInputStream(filename);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            DigestInputStream din = new DigestInputStream(in, sha);
            while(din.read() != -1)
                ; // skip
            din.close();
            byte[] digest = sha.digest();

            StringBuilder result = new StringBuilder(filename);
            result.append(": ");
            for (byte b : digest) {
                result.append(String.format("%02X", b));
            }            System.out.println(result);
        }catch (IOException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        List<Thread> ts = new ArrayList<>();
        long startTime = System.nanoTime();

        for(String f : args){
            Thread t = new ComputeDigestT(f);
            t.start();
            ts.add(t);
        }

        for(Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long elapsedTime = System.nanoTime() - startTime;
        System.out.printf("Time: %d ns\n", elapsedTime);
    }
}
