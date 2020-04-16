package cn.com.imageselect.util.http;

import android.os.AsyncTask;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Name: TaskUtil
 * Author: Created by fuliangbo
 * Email: fuliangbo@tieserv.com
 * Date: 2018-05-09 14:53
 */

public class TaskUtil {

    private RequestCallbackListener mListener;
    private HttpUtil mUtil;

    public TaskUtil(RequestCallbackListener mListener) {
        this.mListener = mListener;
        mUtil = HttpUtil.getInstance();
    }

    public void doRequest(String url, Map<String, String> prams, Map<String, File> files, int action) {
        new Task(url, prams, files, action).execute("");
    }

    public void doImgRequest(String url, Map<String, String> prams, Map<String, List<File>> files, int action) {
        new ImageTask(url, prams, files, action).execute("");
    }

    public void doRequest(String url, Map<String, String> prams, Map<String, File> files, String token, int action) {
        new Task(url, prams, files, token, action).execute("");
    }


    public void doGetRequest(String url, int action) {
        new GetTask(url, action).execute("");
    }

    public void doGetRequest(String url, String token, int action) {
        new GetTask(url, token, action).execute("");
    }

    public void dopostjsonRequest(String url, String body, int action) {
        new PostjsonbodyTask(url, body, action).execute("");
    }

    public void dopostjsonRequest(String url, String body, String token, int action) {
        new PostjsonbodyTask(url, body, token, action).execute("");
    }

    public class Task extends AsyncTask<String, String, String> {

        private String url;
        private Map<String, String> prams;
        private Map<String, File> files;
        private int action;
        private String token;

        public Task(String url, Map<String, String> prams, Map<String, File> files, int action) {
            this.url = url;
            this.prams = prams;
            this.files = files;
            this.action = action;
        }

        public Task(String url, Map<String, String> prams, Map<String, File> files, String token, int action) {
            this.url = url;
            this.prams = prams;
            this.files = files;
            this.action = action;
            this.token = token;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (files == null) {
                    if (token == null || token.equals("")) {
                        return mUtil.postHttpString(url, prams);
                    } else {
                        return mUtil.postHttpString(url, prams, token);
                    }
                } else {
                    if (token == null || token.equals("")) {
                        return mUtil.postHttpString(url, prams, files);
                    } else {
                        return mUtil.postHttpString(url, prams, files, token);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mListener != null) {
                if (null != s && !s.equals("")) {
                    mListener.doResult(s, action);
                } else {
                    mListener.onErr("err");
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public class GetTask extends AsyncTask<String, String, String> {

        private String url;
        private Map<String, File> files;
        private int action;
        private String token;

        public GetTask(String url, int action) {
            this.url = url;
            this.files = files;
            this.action = action;
        }

        public GetTask(String url, String token, int action) {
            this.url = url;
            this.files = files;
            this.action = action;
            this.token = token;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (token == null || token.equals("")) {
                    return mUtil.getHttpString(url);
                } else {
                    return mUtil.getHttpString(url, token);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mListener != null) {
                if (null != s && !s.equals("")) {
                    mListener.doResult(s, action);
                } else {
                    mListener.onErr("err");
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    public class PostjsonbodyTask extends AsyncTask<String, String, String> {

        private String url;
        private int action;
        private String json;
        private String token;

        public PostjsonbodyTask(String url, String json, int action) {
            this.url = url;
            this.action = action;
            this.json = json;
        }

        public PostjsonbodyTask(String url, String json, String token, int action) {
            this.url = url;
            this.action = action;
            this.json = json;
            this.token = token;
        }


        @Override
        protected String doInBackground(String... strings) {
            try {
                if (token == null || token.equals("")) {
                    return mUtil.postjsonBody(url, json);
                } else {
                    return mUtil.postjsonBody(url, json, token);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mListener != null) {
                if (null != s && !s.equals("")) {
                    mListener.doResult(s, action);
                } else {
                    mListener.onErr("err");
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    public class ImageTask extends AsyncTask<String, String, String> {

        private String url;
        private Map<String, String> prams;
        private Map<String, File> files;
        private Map<String, List<File>> listFiles;
        private int action;
        private String token;

        public ImageTask(String url, Map<String, String> prams, Map<String, List<File>> listFiles, int action) {
            this.url = url;
            this.prams = prams;
            this.listFiles = listFiles;
            this.action = action;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (listFiles != null) {
                    return mUtil.postHttpImgs(url, prams, listFiles);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mListener != null) {
                if (null != s && !s.equals("")) {
                    mListener.doResult(s, action);
                } else {
                    mListener.onErr("err");
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


}
