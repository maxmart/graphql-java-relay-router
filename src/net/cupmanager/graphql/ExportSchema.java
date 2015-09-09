package net.cupmanager.graphql;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExportSchema {
	public static void main(String[] args) {
		Map<String, Object> result = GraphQLTest.exec(query, Collections.emptyMap());
		Map<String,Object> ut = new HashMap<String,Object>();
		ut.put("data", result);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		String out = gson.toJson(ut);
		
		System.out.println(out);
		
	}
	
	
	
	private static String query = "  query IntrospectionQuery {  "+
		"    __schema {  "+
		"      queryType { name }  "+
		"      mutationType { name }  "+
		"      types {  "+
		"        ...FullType        "+
		"      }        "+
		"      directives {        "+
		"        name        "+
		"        description        "+
		"        args {        "+
		"          ...InputValue        "+
		"        }        "+
		"        onOperation        "+
		"        onFragment        "+
		"        onField        "+
		"      }        "+
		"    }        "+
		"  }        "+
		"  fragment FullType on __Type {        "+
		"    kind        "+
		"    name        "+
		"    description        "+
		"    fields {        "+
		"      name        "+
		"      description        "+
		"      args {        "+
		"        ...InputValue        "+
		"      }        "+
		"      type {        "+
		"        ...TypeRef        "+
		"      }        "+
		"      isDeprecated        "+
		"      deprecationReason        "+
		"    }        "+
		"    inputFields {        "+
		"      ...InputValue        "+
		"    }        "+
		"    interfaces {        "+
		"      ...TypeRef        "+
		"   }        "+
		"    enumValues {        "+
		"      name        "+
		"      description        "+
		"      isDeprecated        "+
		"      deprecationReason        "+
		"    }        "+
		"    possibleTypes {        "+
		"      ...TypeRef        "+
		"    }        "+
		"  }        "+
		"  fragment InputValue on __InputValue {        "+
		"    name        "+
		"    description        "+
		"    type { ...TypeRef }        "+
		"    defaultValue        "+
		"  }        "+
		"  fragment TypeRef on __Type {        "+
		"    kind        "+
		"    name        "+
		"    ofType {        "+
		"      kind        "+
		"      name        "+
		"      ofType {        "+
		"        kind        "+
		"        name        "+
		"        ofType {        "+
		"          kind        "+
		"          name        "+
		"        }        "+
		"      }        "+
		"    }        "+
		"  }        ";
}
