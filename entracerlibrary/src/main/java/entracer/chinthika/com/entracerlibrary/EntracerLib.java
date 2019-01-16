package entracer.chinthika.com.entracerlibrary;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EntracerLib {
    private String token;
    private boolean logging;
    private OkHttpClient client;
    private Map<String, String> concurrentHashMap;
    private ExecutorService executor;
    private Gson gson;


    public EntracerLib(String token, boolean logging) {
        this.token = token;
        this.logging = logging;
        this.client = new OkHttpClient();
        this.gson = new Gson();
        concurrentHashMap = new ConcurrentHashMap<>();
        executor = Executors.newFixedThreadPool(10);
        if (!auth(token)) {
            logger("Entracer", "Cannot Authenticate Using Token", "E");
            this.concurrentHashMap = null;
            this.executor = null;
        } else {
            logger("Entracer", "Authenticated Successfully Using Token ".concat(token), "I");
        }
    }

    private String getTag() {
        return UUID.randomUUID().toString();
    }

    private void logger(String tag, String msg, String type) {
        if (logging) {
            if (type.equalsIgnoreCase("E")) {
                Log.e(tag, msg);
            } else {
                Log.i(tag, msg);
            }
        }
    }

    private boolean auth(final String token) {
        final String tag = getTag();
        final String url = "http://crm.orete.org/api/v1/people";
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    concurrentHashMap.put(tag, response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                    concurrentHashMap.put(tag, e.toString());
                }
            }
        });

        while (!concurrentHashMap.containsKey(tag)) ;
        String result = concurrentHashMap.get(tag).replace("{\"people\":", "");
        concurrentHashMap.remove(tag);

        try {
            gson.fromJson(result.substring(0, result.length() - 1), Person[].class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean createPersonCheckRequiredFields(Person person) {
        if (person.getEmail() != null && person.getFirst_name() != null && person.getType() != null && person.getDo_not_call() != null) {
            return true;
        } else {
            return false;
        }
    }

    private RequestBody createPersonRequestBody(Person person) {
        FormBody.Builder body = new FormBody.Builder();

        if (person.getType() != null) {
            body.add("person[type]", person.getType());
        }
        if (person.getFirst_name() != null) {
            body.add("person[first_name]", person.getFirst_name());
        }
        if (person.getLast_name() != null) {
            body.add("person[last_name]", person.getLast_name());
        }
        if (person.getEmail() != null) {
            body.add("person[email]", person.getEmail());
        }
        if (person.getMobile() != null) {
            body.add("person[mobile]", person.getMobile());
        }
        if (person.getStatus() != null) {
            body.add("person[status]", person.getStatus());
        }
        if (person.getRating() != null) {
            body.add("person[rating]", person.getRating());
        }
        if (person.getDepartment() != null) {
            body.add("person[department]", person.getDepartment());
        }
        if (person.getDo_not_call() != null) {
            body.add("person[do_not_call]", person.getDo_not_call());
        }
        if (person.getTitle() != null) {
            body.add("person[title]", person.getTitle());
        }
        if (person.getJob_title() != null) {
            body.add("person[job_title]", person.getJob_title());
        }
        if (person.getReferred_by() != null) {
            body.add("person[referred_by]", person.getReferred_by());
        }
        if (person.getTarget_value() != null) {
            body.add("person[target_value]", person.getTarget_value());
        }
        if (person.getAltenative_email() != null) {
            body.add("person[altenative_email]", person.getAltenative_email());
        }
        if (person.getAltenative_phone() != null) {
            body.add("person[altenative_phone]", person.getAltenative_phone());
        }
        if (person.getStreet_1() != null) {
            body.add("person[street_1]", person.getStreet_1());
        }
        if (person.getStreet_2() != null) {
            body.add("person[street_2]", person.getStreet_2());
        }
        if (person.getCity() != null) {
            body.add("person[city]", person.getCity());
        }
        if (person.getState() != null) {
            body.add("person[state]", person.getState());
        }
        if (person.getZip_code() != null) {
            body.add("person[zip_code]", person.getZip_code());
        }
        if (person.getCountry() != null) {
            body.add("person[country]", person.getCountry());
        }
        if (person.getWebsite() != null) {
            body.add("person[website]", person.getWebsite());
        }
        if (person.getTwitter() != null) {
            body.add("person[twitter]", person.getTwitter());
        }
        if (person.getLinkedin() != null) {
            body.add("person[linkedin]", person.getLinkedin());
        }
        if (person.getFacebook() != null) {
            body.add("person[facebook]", person.getFacebook());
        }
        if (person.getSkype() != null) {
            body.add("person[skype]", person.getSkype());
        }
        if (person.getComments() != null) {
            body.add("person[comments]", person.getComments());
        }
        //TODO assignee,custom_fields,organizations,tags

        return body.build();
    }

    public Person createPerson(Person person) {
        final String tag = getTag();
        final String url = "http://crm.orete.org/api/v1/people";
        if (createPersonCheckRequiredFields(person)) {
            final RequestBody body = createPersonRequestBody(person);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Request request = new Request.Builder()
                                .url(url)
                                .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                                .post(body)
                                .build();
                        Response response = client.newCall(request).execute();
                        if (response.code() == 201)
                            concurrentHashMap.put(tag, response.body().string());
                        else
                            concurrentHashMap.put(tag, "Error");
                    } catch (Exception e) {
                        e.printStackTrace();
                        concurrentHashMap.put(tag, e.toString());
                    }
                }
            });

            while (!concurrentHashMap.containsKey(tag)) ;
            String result = concurrentHashMap.get(tag);
            concurrentHashMap.remove(tag);

            if (result.equals("Error")) {
                logger("Entracer", "Person cannot be created with email ".concat(person.getEmail()), "E");
                return null;
            } else {
                logger("Entracer", "Person created with email ".concat(person.getEmail()), "I");
                result = result.replace("{\"person\":", "");
                return gson.fromJson(result.substring(0, result.length() - 1), Person.class);
            }
        } else {
            logger("Entracer", "Required fields are empty", "E");
            return null;
        }
    }

    public Person updatePerson(String id, Person person) {
        final String tag = getTag();
        final String url = new StringBuffer("http://crm.orete.org/api/v1/people/").append(id).toString();
        if (createPersonCheckRequiredFields(person)) {
            final RequestBody body = createPersonRequestBody(person);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Request request = new Request.Builder()
                                .url(url)
                                .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                                .put(body)
                                .build();
                        Response response = client.newCall(request).execute();
                        if (response.code() == 200)
                            concurrentHashMap.put(tag, response.body().string());
                        else
                            concurrentHashMap.put(tag, "Error");
                    } catch (Exception e) {
                        e.printStackTrace();
                        concurrentHashMap.put(tag, e.toString());
                    }
                }
            });

            while (!concurrentHashMap.containsKey(tag)) ;
            String result = concurrentHashMap.get(tag);
            concurrentHashMap.remove(tag);

            if (result.equals("Error")) {
                logger("Entracer", "Person cannot be updated with id ".concat(id), "E");
                return null;
            } else {
                logger("Entracer", "Person updated with id ".concat(id), "I");
                result = result.replace("{\"person\":", "");
                return gson.fromJson(result.substring(0, result.length() - 1), Person.class);
            }
        } else {
            logger("Entracer", "Required fields are empty", "E");
            return null;
        }
    }

    public Person getPersonById(String id) {
        final String tag = getTag();
        final String url = new StringBuffer("http://crm.orete.org/api/v1/people/").append(id).toString();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        concurrentHashMap.put(tag, response.body().string());
                    else
                        concurrentHashMap.put(tag, "Error");
                } catch (Exception e) {
                    e.printStackTrace();
                    concurrentHashMap.put(tag, e.toString());
                }
            }
        });

        while (!concurrentHashMap.containsKey(tag)) ;

        String result = concurrentHashMap.get(tag);
        concurrentHashMap.remove(tag);

        if (result.equals("Error")) {
            logger("Entracer", "Person not found with id ".concat(id), "E");
            return null;
        } else {
            logger("Entracer", "Person found with id ".concat(id), "I");
            result = result.replace("{\"person\":", "");
            return gson.fromJson(result.substring(0, result.length() - 1), Person.class);
        }
    }

    public Person getPersonByEmail(final String email) {
        final String tag = getTag();
        final String url = "http://crm.orete.org/api/v1/people/find_by_email";
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = new FormBody.Builder().add("email", email).build();
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        concurrentHashMap.put(tag, response.body().string());
                    else
                        concurrentHashMap.put(tag, "Error");
                } catch (Exception e) {
                    e.printStackTrace();
                    concurrentHashMap.put(tag, e.toString());
                }
            }
        });

        while (!concurrentHashMap.containsKey(tag)) ;
        String result = concurrentHashMap.get(tag);
        concurrentHashMap.remove(tag);

        if (result.equals("Error")) {
            logger("Entracer", "Person not found with email ".concat(email), "E");
            return null;
        } else {
            logger("Entracer", "Person found with email ".concat(email), "I");
            result = result.replace("{\"person\":", "");
            return gson.fromJson(result.substring(0, result.length() - 1), Person.class);
        }
    }

    public boolean deletePersonById(String id) {
        final String tag = getTag();
        final String url = new StringBuffer("http://crm.orete.org/api/v1/people/").append(id).toString();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                            .delete()
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        concurrentHashMap.put(tag, response.body().string());
                    else
                        concurrentHashMap.put(tag, "Error");
                } catch (Exception e) {
                    e.printStackTrace();
                    concurrentHashMap.put(tag, e.toString());
                }
            }
        });

        while (!concurrentHashMap.containsKey(tag)) ;
        String result = concurrentHashMap.get(tag);
        concurrentHashMap.remove(tag);
        if (!result.equals("Error")) {
            logger("Entracer", "Person deleted with id ".concat(id), "I");
            return true;
        } else {
            logger("Entracer", "Person not found for deleting with id ".concat(id), "E");
            return false;
        }
    }

    public Person[] getAllPersons() {
        final String tag = getTag();
        final String url = "http://crm.orete.org/api/v1/people";
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        concurrentHashMap.put(tag, response.body().string());
                    else
                        concurrentHashMap.put(tag, "Error");
                } catch (Exception e) {
                    e.printStackTrace();
                    concurrentHashMap.put(tag, e.toString());
                }
            }
        });

        while (!concurrentHashMap.containsKey(tag)) ;
        String result = concurrentHashMap.get(tag);
        concurrentHashMap.remove(tag);
        if (!result.equals("Error")) {
            if (result.trim().equals("[]}")) {
                logger("Entracer", "There are no people yet", "I");
                return null;
            } else {
                logger("Entracer", "Found some people", "I");
                result = result.replace("{\"people\":", "");
                return gson.fromJson(result.substring(0, result.length() - 1), Person[].class);
            }
        } else {
            logger("Entracer", "Error occurred while fetching people", "E");
            return null;
        }
    }


    public Organization getOrganisationById(String id) {
        final String tag = getTag();
        final String url = new StringBuffer("http://crm.orete.org/api/v1/organisations/").append(id).toString();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        concurrentHashMap.put(tag, response.body().string());
                    else
                        concurrentHashMap.put(tag, "Error");
                } catch (Exception e) {
                    e.printStackTrace();
                    concurrentHashMap.put(tag, e.toString());
                }
            }
        });

        while (!concurrentHashMap.containsKey(tag)) ;
        String result = concurrentHashMap.get(tag);
        concurrentHashMap.remove(tag);
        if (result.equals("Error")) {
            logger("Entracer", "Organization not found with id ".concat(id), "E");
            return null;
        } else {
            logger("Entracer", "Organization found with id ".concat(id), "I");
            result = result.replace("{\"organisation\":", "");
            return gson.fromJson(result.substring(0, result.length() - 1), Organization.class);
        }
    }

    public boolean deleteOrganisationById(String id) {
        final String tag = getTag();
        final String url = new StringBuffer("http://crm.orete.org/api/v1/organisations/").append(id).toString();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                            .delete()
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        concurrentHashMap.put(tag, response.body().string());
                    else
                        concurrentHashMap.put(tag, "Error");
                } catch (Exception e) {
                    e.printStackTrace();
                    concurrentHashMap.put(tag, e.toString());
                }
            }
        });

        while (!concurrentHashMap.containsKey(tag)) ;
        String result = concurrentHashMap.get(tag);
        concurrentHashMap.remove(tag);
        if (!result.equals("Error")) {
            logger("Entracer", "Organization deleted with id ".concat(id), "I");
            return true;
        } else {
            logger("Entracer", "Organization not found for deleting with id ".concat(id), "E");
            return false;
        }
    }


    public Organization[] getAllOrganisations() {
        final String tag = getTag();
        final String url = "http://crm.orete.org/api/v1/organisations";
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", new StringBuffer("Token token=").append(token).toString())
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                        concurrentHashMap.put(tag, response.body().string());
                    else
                        concurrentHashMap.put(tag, "Error");
                } catch (Exception e) {
                    e.printStackTrace();
                    concurrentHashMap.put(tag, e.toString());
                }
            }
        });

        while (!concurrentHashMap.containsKey(tag)) ;
        String result = concurrentHashMap.get(tag);
        concurrentHashMap.remove(tag);
        if (!result.equals("Error")) {
            if (result.trim().equals("{\"organisations\":[]}")) {
                logger("Entracer", "There are no organizations yet", "E");
                return null;
            } else {
                logger("Entracer", "Found some organizations", "I");
                result = result.replace("{\"organisations\":", "");
                return gson.fromJson(result.substring(0, result.length() - 1), Organization[].class);
            }
        } else {
            logger("Entracer", "Error occurred while fetching organizations", "E");
            return null;
        }
    }

}