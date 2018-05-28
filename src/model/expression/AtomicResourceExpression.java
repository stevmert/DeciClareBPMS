package model.expression;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import model.constraint.ParsingCache;
import model.resource.Resource;
import model.resource.ResourceRole;

public class AtomicResourceExpression implements LogicalExpression, ResourceExpression, Serializable {

	private static final long serialVersionUID = -8585945748968895732L;

	private Resource resource;

	public AtomicResourceExpression(Resource resource) {
		super();
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public int getNrOfElements() {
		return 1;
	}

	@Override
	public HashSet<Resource> getUsedResources() {
		HashSet<Resource> tmp = new HashSet<>();
		tmp.add(resource);
		return tmp;
	}

	@Override
	public boolean contains(Resource r, Set<Resource> resourcesCollection) {
		if(r.equals(resource))
			return true;
		for(Resource x : resourcesCollection)
			if(x instanceof ResourceRole
					&& x.equals(resource)
					&& ((ResourceRole) x).isGeneralizationOf(r))
				return true;
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtomicResourceExpression other = (AtomicResourceExpression) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(resource instanceof ResourceRole)
			return ((ResourceRole) resource).toString(false);
		return resource.toString();
	}

	public static ResourceExpression parseAtomicResourceExpression(String input, ParsingCache pc) {
		return new AtomicResourceExpression(ResourceRole.parseResource(input, pc));
	}
}