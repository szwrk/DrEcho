# Standardizing the Use of Mappers Between Layers

## Status

- Accepted->**In use**

## Context
Need standardize buissness object mapping.

## Decision
Use three types of mappers:
- Client Side, ViewModel <-> ConnectorLayer Mapper (only VM layer known about mapper)
- Backend side, ServiceLayer <-> Domain Mapper
- Backend side, Domain <-> PersistenceLayer Mapper,

## Consequences

**Pros:**
- Separate of concerns
- promises greater long-term flexibility

**Cons:**
-

## Others options
- 

## UML
### Client - ViewModel <-> ConnectorLayer 
``` plantuml
header Case 1
title Rule: Mapping between View Model (VM) and DTO in the VM Layer 

group addItem() action
	group prepare item to send to service
		cli.pre.vm -> cli.app.mapdto : toDto(vm)
		cli.app.mapdto -> cli.pre.vm : return dto
	end
	
	group action - request
		cli.pre.vm -> connectors.service : save(dto)
		connectors.service -> cli.pre.vm : dto getItem(int)
	end
	
	group prepare item to receive by view model layer
		cli.pre.vm -> cli.app.mapdto : toVm(dto)
		cli.app.mapdto -> cli.pre.vm : return vm
	end
end
```
### Backend - ServiceLayer <-> Domain
``` plantuml
header Case 2
title  Passing DTO to the Domain Layer

group addSomeItem(dto)
	group pass value - mapping to domain
		serviceLayer -> service.mapper : toDomain(dto)
		service.mapper -> serviceLayer : return domainObj
	end
	
	group action - pass to domain layer
		serviceLayer -> domainLayer : save(domainObj)
		domainLayer -> serviceLayer : domainObj getItem(int)
	end
	
	group return value - mapping to dto
		serviceLayer -> service.mapper : toDto(domainObj)
		service.mapper -> serviceLayer : return dto
	end
end
```

### Backend - Domain <-> PersistenceLayer
``` plantuml
header Case 3
title  Persisting Domain object

group save(entity)
	group pass value - mapping domain to entity
		domainLayer -> repo.mapper : toRepo(entity)
		repo.mapper -> domainLayer : return domainObj
	end
	
	group action - pass to repository layer
		domainLayer -> persistanceLayer : save(entity)
		persistanceLayer -> domainLayer : entity getItem(int)
	end
	
	group return value - mapping entity to domain
		domainLayer -> repo.mapper : toDomain(entity)
		repo.mapper -> domainLayer : return domainObj
	end
end
```

