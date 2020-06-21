
package com.hooply;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExternalDb {
    private Object lock1;

    public static void insertRemoteUser(User user) {
        final Object lock = new Object();
        final User sss = user;
        final boolean[] found = {false};
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok";
        Thread thread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    URL url = null;
                    try {
                        url = new URL("https://caracal.imada.sdu.dk/app2020/users");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type", "application/json");
                        con.setRequestProperty("Authorization", "Bearer " + token);
                        con.setDoOutput(true);
                        Gson gson = new Gson();
                        String json = gson.toJson(sss);
                        Log.d("json", json);
                        OutputStream os = con.getOutputStream();
                        os.write(json.toString().getBytes("UTF-8"));
                        os.close();
                        Log.d("responde", String.valueOf(con.getResponseCode()));

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    found[0] = true;
                    lock.notify();
                }
            }
        });
        thread.start();
        try {
            synchronized (lock) {
                while (found[0] == false) {
                    lock.wait();
                }
                // add remote db data to local db ???
                return;
            }
        } catch (InterruptedException e) {
            // maybe do smth for exception handling ? or just ignore lol
            return;
        }

    }

    public static List<Post> getPosts(int amount) {
        final int number = amount;
        final Object lock = new Object();
        final List<Post>[] posts = new List[]{new ArrayList<Post>()};
        final boolean[] found = {false};

        Thread thread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    URL url = null;
                    try {
                        url = new URL("https://caracal.imada.sdu.dk/app2020/posts?order=stamp.desc&limit="+String.valueOf(number));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        String test = con.getResponseMessage();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder out = new StringBuilder();
                        String line;


                        final int bufferSize = 1024;
                        final char[] buffer = new char[bufferSize];
                        Reader in = new InputStreamReader(con.getInputStream());
                        int charsRead;
                        while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
                            out.append(buffer, 0, charsRead);
                        }

                        Log.d("testgettingpost",out.toString());

                        List<Post> stuff = Parser.parsePost(out.toString(),MainActivity.db.myDao());
                        posts[0] = stuff;

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    found[0] = true;
                    lock.notify();
                }
            }
        });

        thread.start();

        try {
            synchronized(lock) {
                while(found[0] == false) {
                    lock.wait();
                }
            }
        } catch (InterruptedException e) {

        }
        return posts[0];
    }

    static void insertPost(Post post) {
        final Object lock = new Object();
        final Post sss = post;
        final boolean[] found = {false};
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok";
        Thread thread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    URL url = null;
                    try {
                        url = new URL("https://caracal.imada.sdu.dk/app2020/posts");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type", "application/json");
                        con.setRequestProperty("Authorization", "Bearer " + token);
                        con.setDoOutput(true);
                        Gson gson = new Gson();
                        String json = gson.toJson(sss);
                        Log.d("json2", json);
                        OutputStream os = con.getOutputStream();
                        os.write(json.toString().getBytes("UTF-8"));
                        os.close();
                        Log.d("insertpost", String.valueOf(con.getResponseCode()));

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    found[0] = true;
                    lock.notify();
                }
            }
        });
        thread.start();
        try {
            synchronized (lock) {
                while (found[0] == false) {
                    lock.wait();
                }
                // add remote db data to local db ???
                return;
            }
        } catch (InterruptedException e) {
            // maybe do smth for exception handling ? or just ignore lol
            return;
        }
    }

    static void insertComment(Comments comment) {
        final Object lock = new Object();
        final Comments sss = comment;
        final boolean[] found = {false};
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAyMCJ9.PZG35xIvP9vuxirBshLunzYADEpn68wPgDUqzGDd7ok";
        Thread thread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    URL url = null;
                    try {
                        url = new URL("https://caracal.imada.sdu.dk/app2020/comments");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Content-Type", "application/json");
                        con.setRequestProperty("Authorization", "Bearer " + token);
                        con.setDoOutput(true);
                        Gson gson = new Gson();
                        String json = gson.toJson(sss);
                        Log.d("json", json);
                        OutputStream os = con.getOutputStream();
                        os.write(json.toString().getBytes("UTF-8"));
                        os.close();
                        Log.d("responde", String.valueOf(con.getResponseCode()));

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    found[0] = true;
                    lock.notify();
                }
            }
        });
        thread.start();
        try {
            synchronized (lock) {
                while (found[0] == false) {
                    lock.wait();
                }
                // add remote db data to local db ???
                return;
            }
        } catch (InterruptedException e) {
            // maybe do smth for exception handling ? or just ignore lol
            return;
        }
    }

    public static List<Comments> getComments(final String urlquery) {
        final Object lock = new Object();
        final List<Comments>[] posts = new List[]{new ArrayList<Comments>()};
        final boolean[] found = {false};

        Thread thread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    URL url = null;
                    try {
                        url = new URL(urlquery);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        String test = con.getResponseMessage();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder out = new StringBuilder();
                        String line;
                        final int bufferSize = 1024;
                        final char[] buffer = new char[bufferSize];
                        Reader in = new InputStreamReader(con.getInputStream());
                        int charsRead;
                        while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
                            out.append(buffer, 0, charsRead);
                        }

                        Log.d("testgettingpostcomments",out.toString());
                        List<Comments> stuff = Parser.parseComments(out.toString(),MainActivity.db.myDao());
                        posts[0] = stuff;

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    found[0] = true;
                    lock.notify();
                }
            }
        });

        thread.start();

        try {
            synchronized(lock) {
                while(found[0] == false) {
                    lock.wait();
                }
            }
        } catch (InterruptedException e) {

        }
        return posts[0];
    }



}





