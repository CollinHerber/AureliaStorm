import { customElement } from 'aurelia-framework';

@customElement("completion-custom-element")
export class MyController extends MyControllerParent{
	public publicProperty: string;
	private privateProperty: string;

	@bindable
	public publicPropertyWithBinding: string;
	@bindable
	private privatePropertyWithBinding: string;
}

export class MyControllerParent {
	public inheritedProperty: string;
	@bindable
	public inheritedPropertyWithBinding: string;
}