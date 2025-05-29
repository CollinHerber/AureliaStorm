import { customElement } from 'aurelia-framework';

@customElement("completion-custom-element")
export class MyController {
	public publicProperty: string;
	private privateProperty: string;

	@bindable
	public publicPropertyWithBinding: string;
	@bindable
	private privatePropertyWithBinding: string;
}